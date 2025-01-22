package com.qre.tg.query.api.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.qre.cmel.cache.service.HazelcastCacheService;
import com.qre.cmel.ecommsvcs.sdk.dto.MessageDto;
import com.qre.cmel.ecommsvcs.sdk.service.ECommSvcService;
import com.qre.cmel.exception.EntryExitMismatchException;
import com.qre.cmel.exception.InvalidJourneyTypeException;
import com.qre.cmel.exception.InvalidTicketException;
import com.qre.cmel.message.sdk.service.MessageService;
import com.qre.cmel.security.component.Crypto;
import com.qre.cmel.security.component.DigitalSignature;
import com.qre.tg.dao.route.TrainRouteRepository;
import com.qre.tg.dao.ticket.JourneyDetailsRepository;
import com.qre.tg.dao.ticket.TicketMasterRepository;
import com.qre.tg.dto.base.JsonFieldName;
import com.qre.tg.dto.qr.PurchaseTicketRequest;
import com.qre.tg.dto.qr.TicketDetailResponse;
import com.qre.tg.dto.qr.TicketUpdateStatusRequest;
import com.qre.tg.entity.route.Station;
import com.qre.tg.entity.ticket.*;
import com.qre.tg.query.api.common.JourneyTypeEnum;
import com.qre.tg.query.api.common.TicketStatusEnum;
import com.qre.tg.query.api.config.ApplicationProperties;
import com.qre.tg.query.api.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {

    private final Logger logger = LoggerFactory.getLogger(TicketServiceImpl.class);

    private final TicketMasterRepository ticketMasterRepository;

    private final HazelcastCacheService hazelcastCacheService;

    private final DigitalSignature digitalSignature;

    private final Crypto crypto;

    private final ApplicationProperties applicationProperties;

    private final ECommSvcService eCommSvcService;

    private final TrainRouteRepository trainRouteRepository;

    private final JourneyDetailsRepository journeyDetailsRepository;

    String template = "Dear Customer,\n" +
            "We are pleased to confirm that your train ticket purchase for the journey on %s has been successfully processed. " +
            "Thank you for choosing our services for your travel needs. Below are the details of your booking:\n" +
            "Departure Station: %s\n" +
            "Arrival Station: %s\n" +
            "Departure Time: %s\n" +
            "Arrival Time: %s\n" +
            "Ticket Price: %s";

    @Value("${app.producer.purchase.queue}")
    private String queue;

    private final MessageService messageService;

    @Override
    public TicketDetailResponse purchaseTicket(PurchaseTicketRequest request) throws Exception {

        TicketMaster ticketMaster = TicketMaster.builder()
                .ticketType(request.getTicketType())
                .journeyTypeId(request.getJourneyType())
                .serialNumber(generateSerialNumber(request))
                .issuerId(request.getIssuerId())
                .creatorId(5) //tg_id
                .groupSize(request.getGroupSize())
                .phoneNo(request.getPhoneNo())
                .email(request.getEmail())
                .creationDateTime(new Date(request.getCreationDatetime()))
                .effectiveDateTime(new Date(request.getStartDatetime()))
                .operatorId(request.getOperatorId()) // transport operator
                .validityDomain(1) // train/bus/flight/all
                .validityPeriod(getValidityPeriod(new Date(request.getStartDatetime()),
                        new Date(request.getEndDatetime())))
                .transactionData(TransactionData.builder()
                        .paymentRefNo(request.getPaymentRefNo())
                        .currency(request.getCurrency())
                        .amount(request.getAmount())
                        .build())
                .journeyDetails(getJourneyDetails(request))
                .additionalInfo(AdditionalInfo.builder().build())
                .build();
        Map<String, Object> ticketJsonData = prepareTicketJsonData(ticketMaster);
        ticketMaster.setSecurity(Security.builder()
                .digitalSignature(generateDigitalSignature(ticketJsonData))
                .build());
        ticketMaster.setQrData(prepareQRData(ticketJsonData));
        ticketMaster.setCreatedDatetime(new Date());
        TicketMaster newTicketData = ticketMasterRepository.save(ticketMaster);

        // Send ticket data to ticket validation service
        messageService.send(queue, new ObjectMapper().writeValueAsString(newTicketData));

        if (!request.getEmail().isEmpty()) {

            List<Station> stations = trainRouteRepository.findAll();

            // To generate QR code file using qr data
            CompletableFuture.runAsync(() -> {
                try {
                    String qrData = newTicketData.getSecurity().getDigitalSignature() +
                            "#" + Base64.getEncoder().encodeToString(newTicketData.getQrData());

                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

                    String formattedString = String.format(template,
                            dateFormat.format(request.getCreationDatetime()),
                            stations.stream()
                                    .filter(stn -> stn.getStnId() == request.getDeparturePoint())
                                    .findAny()
                                    .orElse(null)
                                    .getStnFullName(),
                            stations.stream()
                                    .filter(stn -> stn.getStnId() == request.getArrivalPoint())
                                    .findAny()
                                    .orElse(null)
                                    .getStnFullName(),
                            dateFormat.format(request.getStartDatetime()),
                            dateFormat.format(request.getEndDatetime()),
                            "$".concat(String.valueOf(request.getAmount())));

                    MessageDto messageDto = MessageDto.builder()
                            .message(formattedString)
                            .subject("QR Ticket Confirmation")
                            .to(request.getEmail())
                            .imageBytes(generateQRCodeImageBytes(qrData, 250, 250))
                            .fileNameWithExtension(newTicketData.getSerialNumber().concat(".png"))
                            .build();

                    eCommSvcService.send(messageDto);
                } catch (Exception e) {
                    e.getStackTrace();
                }
            });
        }

        TicketDetailResponse ticketDetailResponse = TicketDetailResponse.builder()
                .qrData(newTicketData.getQrData().toString())
                .serialNumber(newTicketData.getSerialNumber())
                .departurePoint(request.getDeparturePoint())
                .arrivalPoint(request.getArrivalPoint())
                .status(TicketStatusEnum.ACTIVE.getValue())
                .effectiveDatetime(newTicketData.getEffectiveDateTime().getTime())
                .journeyType(request.getJourneyType())
                .journeyId(String.valueOf(newTicketData.getJourneyTypeId()))
                .build();
        return ticketDetailResponse;
    }


    @Override
    public List<TicketDetailResponse> findAllByEmail(String email) {


        List<Station> stations = trainRouteRepository.findAll();

        return ticketMasterRepository.findAllByEmail(email).stream()
                .filter(Objects::nonNull) // Filter out any null TicketMaster objects
                .map(ticketMaster -> {
                    JourneyDetails journeyDetail = ticketMaster.getJourneyDetails().stream().findFirst().orElse(null);
                    if (journeyDetail == null) {
                        // Handle the case where journeyDetails is empty
                        throw new NullPointerException("Invalid journey details"); // Or throw an exception if this is unexpected
                    }
                    return TicketDetailResponse.builder()
                            .serialNumber(ticketMaster.getSerialNumber())
                            .qrData(ticketMaster.getSecurity().getDigitalSignature() +
                                    "#" + Base64.getEncoder().encodeToString(ticketMaster.getQrData()))
                            .effectiveDatetime(ticketMaster.getEffectiveDateTime().getTime())
                            .journeyType(ticketMaster.getJourneyTypeId())
                            .arrivalPoint(journeyDetail.getArrivalPoint())
                            .departurePoint(journeyDetail.getDeparturePoint())
                            .status(journeyDetail.getStatus())
                            .arrivalPointDes(stations.stream()
                                    .filter(stn -> stn.getStnId() == journeyDetail.getArrivalPoint())
                                    .findAny()
                                    .orElse(null)
                                    .getStnFullName())
                            .paymentRefNo(ticketMaster.getTransactionData().getPaymentRefNo())
                            .amount(ticketMaster.getTransactionData().getAmount())
                            .departurePointDes(stations.stream()
                                    .filter(stn -> stn.getStnId() == journeyDetail.getDeparturePoint())
                                    .findAny()
                                    .orElse(null)
                                    .getStnFullName())
                            .build();
                })
                .collect(Collectors.toList());
    }

    protected Map<String, Object> prepareTicketJsonData(TicketMaster ticketMaster) throws JsonProcessingException {

        Map<String, Object> ticket = new HashMap<>();
        Map<String, Object> ticketDetail = new HashMap<>();
        ticketDetail.put(JsonFieldName.TICKET_TYPE, ticketMaster.getTicketType());
        ticketDetail.put(JsonFieldName.JOURNEY_TYPE, ticketMaster.getJourneyTypeId());
        ticketDetail.put(JsonFieldName.SERIAL_NUMBER, ticketMaster.getSerialNumber());
        ticketDetail.put(JsonFieldName.ISSUER_ID, ticketMaster.getIssuerId());
        ticketDetail.put(JsonFieldName.CREATOR_ID, ticketMaster.getCreatorId());
        ticketDetail.put(JsonFieldName.GROUP_SIZE, ticketMaster.getGroupSize());
        ticketDetail.put(JsonFieldName.PHONE_NO, ticketMaster.getPhoneNo());
        ticketDetail.put(JsonFieldName.EMAIL, ticketMaster.getEmail());
        ticketDetail.put(JsonFieldName.EFFECTIVE_DATE_TIME, ticketMaster.getEffectiveDateTime().getTime());
        ticketDetail.put(JsonFieldName.OPERATOR_ID, ticketMaster.getOperatorId());
        ticketDetail.put(JsonFieldName.VALIDITY_DOMAIN, ticketMaster.getValidityDomain());
        ticketDetail.put(JsonFieldName.VALIDITY_PERIOD, ticketMaster.getValidityPeriod());
        ticketDetail.put(JsonFieldName.TRANSACTION_DATA, ticketMaster.getTransactionData());
        ticket.put(JsonFieldName.TICKET_DETAIL, ticketDetail);
        return ticket;
    }

    protected byte[] prepareQRData(Map<String, Object> ticketJsonData) throws Exception {

        String jsonData  = new ObjectMapper().writeValueAsString(ticketJsonData);
        logger.info("TicketServiceImpl.prepareQRData ticketJsonData : {}", jsonData);

        return crypto.aesEncrypt(jsonData.getBytes()
                , applicationProperties.getPrivateKeyPath());
    }

    protected List<JourneyDetails> getJourneyDetails(PurchaseTicketRequest request) {

        List<JourneyDetails> journeyDetailsList = new ArrayList<>();

        // Use a switch statement to handle different journey types
        switch (JourneyTypeEnum.fromValue(request.getJourneyType())) {
            case SINGLE:
                JourneyDetails journeyDetails = JourneyDetails.builder()
                        .departurePoint(request.getDeparturePoint())
                        .arrivalPoint(request.getArrivalPoint())
                        .status(TicketStatusEnum.ACTIVE.getValue())
                        .build();
                journeyDetails.setCreatedDatetime(new Date());
                journeyDetailsList.add(journeyDetails);
                break;
            case RETURN_TICKET:
                journeyDetails = JourneyDetails.builder()
                        .departurePoint(request.getDeparturePoint())
                        .arrivalPoint(request.getArrivalPoint())
                        .status(TicketStatusEnum.ACTIVE.getValue())
                        .build();
                journeyDetails.setCreatedDatetime(new Date());
                journeyDetailsList.add(journeyDetails);
                break;
            case GROUP:
                for (int i = 0; i < request.getGroupSize(); i++) {

                    journeyDetails = JourneyDetails.builder()
                            .departurePoint(request.getDeparturePoint())
                            .arrivalPoint(request.getArrivalPoint())
                            .status(TicketStatusEnum.ACTIVE.getValue())
                            .build();
                    journeyDetails.setCreatedDatetime(new Date());
                    journeyDetailsList.add(journeyDetails);
                }
                break;
            default:
                logger.error("Invalid journey type: {}", request.getJourneyType());
                throw new InvalidJourneyTypeException("Invalid journey type: {}" + request.getJourneyType());
        }

        return journeyDetailsList;
    }

    private boolean validateTicket(PurchaseTicketRequest purchaseTicketRequest) {
        return false;
    }

    public String generateSerialNumber(PurchaseTicketRequest request) {

        //  combination with the time of creation, the ticket issuer id, app type and sno
        String sno = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
                + String.format("%02d", request.getIssuerId())
                + request.getAppType().substring(0, 3)
                + String.format("%013d", hazelcastCacheService.getNextValue("SnoAtomicLong"));

        return sno;
    }

    @Override
    public List<TicketDetailResponse> findAllRefundableTicketByEmail(String email) {
        return findAllByEmail(email).stream().filter(data -> data.getStatus().equals(TicketStatusEnum.ACTIVE.getValue())
                || data.getStatus().equals(TicketStatusEnum.REFUND_APPROVED.getValue())).collect(Collectors.toList());
    }

    /**
     * @param startDate
     * @param endDate
     * @return
     */
    protected int getValidityPeriod(Date startDate, Date endDate) {

        // Calculate validity period in milliseconds
        long validityPeriodMillis = endDate.getTime() - startDate.getTime();

        // Convert milliseconds to days
        long validityPeriodDays = validityPeriodMillis / (1000 * 60 * 60 * 24);
        return (int) validityPeriodDays;
    }

    protected String generateDigitalSignature(Map<String, Object> ticket) throws Exception {

        String jsonString = new ObjectMapper().writeValueAsString(ticket);
        byte[] signData = digitalSignature.signingL2(jsonString,
                crypto.getPrivateKey(applicationProperties.getPrivateKeyPath()));
        return Base64.getEncoder().encodeToString(signData);

    }

    public static byte[] generateQRCodeImageBytes(String text, int width, int height) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageConfig con = new MatrixToImageConfig(0xFF000002, 0xFFFFFFFF);

        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream, con);
        byte[] pngData = pngOutputStream.toByteArray();
        return pngData;
    }


    @Override
    public Optional<TicketMaster> findByTransactionDataPaymentRefNo(String paymentRefNo) {
        return ticketMasterRepository.findByTransactionDataPaymentRefNo(paymentRefNo);
    }

    @Override
    public void updateRefund(String serialNumber, TicketStatusEnum ticketStatusEnum) {

        Optional<TicketMaster> ticketMasterOpt = ticketMasterRepository.findAllBySerialNumber(serialNumber);
        if (ticketMasterOpt.isEmpty()) {
            throw new NullPointerException("Invalid ticket[" + serialNumber + "]");
        }

        TicketMaster ticketMaster = ticketMasterOpt.get();
        Optional<JourneyDetails> journeyDetailsOpt = ticketMaster.getJourneyDetails().stream().findFirst();
        if (journeyDetailsOpt.isEmpty()) {
            throw new NullPointerException("Invalid journey of ticket[" + serialNumber + "]");
        }

        JourneyDetails journeyDetails = journeyDetailsOpt.get();
        if (journeyDetails.getStatus().equals(TicketStatusEnum.ACTIVE.getValue())
                || journeyDetails.getStatus().equals(TicketStatusEnum.REFUND_IN_PROGRESS.getValue())) {
            journeyDetails.setStatus(ticketStatusEnum.getValue());
            journeyDetails.setUpdatedDatetime(new Date());
        }
        else {
            throw new IllegalArgumentException("Invalid ticket status");
        }
        journeyDetailsRepository.save(journeyDetails);
    }

    @Override
    public Optional<TicketMaster> findBySerialNumber(String serialNumber) {
        Optional<TicketMaster> ticketMasterOpt = ticketMasterRepository.findAllBySerialNumber(serialNumber);
        if (ticketMasterOpt.isEmpty()) {
            throw new NullPointerException("Invalid ticket[" + serialNumber + "]");
        }

        return Optional.of(ticketMasterOpt.get());
    }

    @Override
    public void updateTicketStatus(TicketUpdateStatusRequest request) {

        Optional<TicketMaster> ticketMasterOpt = ticketMasterRepository.findAllBySerialNumber(request.getSerialNumber());
        if (ticketMasterOpt.isEmpty()) {
            throw new InvalidTicketException("Invalid ticket[" + request.getSerialNumber() + "]");
        }

        TicketMaster ticketMaster = ticketMasterOpt.get();
        Optional<JourneyDetails> journeyDetailsOpt = ticketMaster.getJourneyDetails().stream().findFirst();
        if (journeyDetailsOpt.isEmpty()) {
            throw new InvalidTicketException("Invalid journey of ticket [" + request.getSerialNumber() + "]");
        }

        JourneyDetails journeyDetails = journeyDetailsOpt.get();
        if (request.getStatus().equals(TicketStatusEnum.ENTRY.getValue())) {
            if (!journeyDetails.getStatus().equals(TicketStatusEnum.ACTIVE.getValue())) {
                throw new EntryExitMismatchException("Ticket [" + request.getSerialNumber() + "] is not available for entry");
            }
            journeyDetails.setEntryDateTime(new Date(request.getEntryDateTime()));
            journeyDetails.setStatus(request.getStatus());
            journeyDetails.setUpdatedDatetime(new Date());
        } else if (request.getStatus().equals(TicketStatusEnum.EXIT.getValue())) {
            if (!journeyDetails.getStatus().equals(TicketStatusEnum.ENTRY.getValue())) {
                throw new EntryExitMismatchException("Ticket [" + request.getSerialNumber() + "] is not available for exit");
            }
            journeyDetails.setExitDateTime(new Date(request.getExitDateTime()));
            journeyDetails.setStatus(request.getStatus());
            journeyDetails.setUpdatedDatetime(new Date());
        } else {
            throw new EntryExitMismatchException("Invalid ticket status: " + request.getStatus());
        }
        journeyDetailsRepository.save(journeyDetails);
    }
}
