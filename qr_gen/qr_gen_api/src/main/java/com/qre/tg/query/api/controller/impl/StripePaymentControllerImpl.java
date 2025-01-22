package com.qre.tg.query.api.controller.impl;

import com.qre.tg.dto.base.APIResponse;
import com.qre.tg.dto.base.JsonFieldName;
import com.qre.tg.entity.ticket.TicketMaster;
import com.qre.tg.query.api.common.RefundPolicyTypeEnum;
import com.qre.tg.query.api.common.TicketStatusEnum;
import com.qre.tg.query.api.controller.StripePaymentController;
import com.qre.tg.query.api.service.RefundPolicyService;
import com.qre.tg.query.api.service.TicketService;
import com.qre.tg.query.api.service.impl.RefundPolicyServiceFactoryImpl;
import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.*;
import com.stripe.net.Webhook;
import com.stripe.param.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.*;

@RestController
@RequestMapping("/api/v1/payments")
public class StripePaymentControllerImpl implements StripePaymentController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${stripe.secret.key}")
    private String secretKey;

    // This is your Stripe CLI webhook secret for testing your endpoint locally.
    String endpointSecret = "whsec_a12b94b83b6da81690ec182e6d2f9b4f526f79aa63090ae2740ab31b0caef46b";

    private final TicketService ticketService;

    private final RefundPolicyServiceFactoryImpl refundPolicyServiceFactoryImpl;

    public StripePaymentControllerImpl(TicketService ticketService,
                                       RefundPolicyServiceFactoryImpl refundPolicyServiceFactoryImpl) {
        this.ticketService = ticketService;
        this.refundPolicyServiceFactoryImpl = refundPolicyServiceFactoryImpl;
    }

    @PostConstruct
    public void init() {
        Stripe.apiKey = secretKey;
    }

    /**
     * {
     * "email": "zawminthan@gmail.com",
     * "allowFutureUsage": true,
     * "currency": "sgd",
     * "amount" : 200
     * }
     *
     * @param payLoad
     * @return
     */
    @PostMapping("/CreatePaymentIntent")
    public ResponseEntity<APIResponse> createPaymentIntent(@RequestBody Map<String, Object> payLoad) {

        try {

            String customerId;
            String email = payLoad.get(JsonFieldName.EMAIL).toString();
            boolean allowFutureUsage = (boolean) payLoad.get(JsonFieldName.ALLOW_FUTURE_USAGE);

            CustomerCollection customerCollection =
                    Customer.list(
                            CustomerListParams
                                    .builder()
                                    .setEmail(email)
                                    .setLimit(1L)
                                    .build()
                    );

            //Checks the if the customer exists, if not creates a new customer
            if (customerCollection.getData().size() != 0) {
                customerId = customerCollection.getData().get(0).getId();

            }
            else {
                Customer customer = Customer.create(CustomerCreateParams
                        .builder()
                        .setEmail(email)
                        .build());
                customerId = customer.getId();
            }


            PaymentIntentCreateParams createParams = new
                    PaymentIntentCreateParams.Builder()
                    .setCurrency(payLoad.get(JsonFieldName.CURRENCY).toString())
                    .setAmount(Long.valueOf(payLoad.get(JsonFieldName.AMOUNT).toString()))
                    //.setPaymentMethod("pm_card_visa")
                    .addPaymentMethodType("card")
                    .setCustomer(customerId)
                    .setSetupFutureUsage(allowFutureUsage ?
                            PaymentIntentCreateParams.SetupFutureUsage.ON_SESSION :
                            PaymentIntentCreateParams.SetupFutureUsage.OFF_SESSION)
                    .build();

            PaymentIntent paymentIntent = PaymentIntent.create(createParams);
            logger.info("PaymentController.createPaymentIntent clientSecret:{}", paymentIntent.getClientSecret());
            logger.info("PaymentController.createPaymentIntent getId:{}", paymentIntent.getId());
            logger.info("PaymentController.createPaymentIntent getLatestCharge:{}", paymentIntent.getLatestCharge());
            logger.info("PaymentController.createPaymentIntent getInvoice:{}", paymentIntent.getInvoice());
            Map<String, Object> response = new HashMap<>();
            response.put(JsonFieldName.CLIENT_SECRET, paymentIntent.getClientSecret());
            response.put(JsonFieldName.PAYMENT_INTENT_ID, paymentIntent.getId());

            APIResponse apiResponse = APIResponse.builder()
                    .responseCode(String.valueOf(HttpStatus.OK.value()))
                    .responseMsg(HttpStatus.OK.getReasonPhrase())
                    .responseData(response)
                    .build();
            return ResponseEntity.ok().body(apiResponse);

        } catch (StripeException e) {
            APIResponse apiResponse = APIResponse.builder()
                    .responseCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                    .responseMsg(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                    .responseData(null)
                    .build();
            return ResponseEntity.ok().body(apiResponse);
        }
    }

    /**
     * {
     * "email":"dmpuser100@gmail.com"
     * }
     *
     * @param payLoad
     * @return
     */
    @PostMapping("/FetchCustomerCards")
    public ResponseEntity<APIResponse> fetchCustomerCards(@RequestBody Map<String, Object> payLoad) {
        try {

            String customerId;
            String email = payLoad.get(JsonFieldName.EMAIL).toString();

            CustomerCollection customerCollection =
                    Customer.list(
                            CustomerListParams
                                    .builder()
                                    .setEmail(email)
                                    .setLimit(1L)
                                    .build()
                    );

            List<PaymentMethod> paymentMethods = new ArrayList<>();
            //Checks the if the customer exists, if not creates a new customer
            if (customerCollection.getData().size() != 0) {
                customerId = customerCollection.getData().get(0).getId();

                paymentMethods = PaymentMethod
                        .list(
                                PaymentMethodListParams
                                        .builder()
                                        .setCustomer(customerId)
                                        .setType(PaymentMethodListParams.Type.CARD)
                                        .build()
                        ).getData();
            }

            APIResponse apiResponse = APIResponse.builder()
                    .responseCode(String.valueOf(HttpStatus.OK.value()))
                    .responseMsg(HttpStatus.OK.getReasonPhrase())
                    .responseData(paymentMethods)
                    .build();
            return ResponseEntity.ok().body(apiResponse);

        } catch (StripeException e) {

            APIResponse apiResponse = APIResponse.builder()
                    .responseCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                    .responseMsg(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                    .responseData(null)
                    .build();
            return ResponseEntity.ok().body(apiResponse);
        }
    }

    @PostMapping("/DeletePaymentMethod")
    public ResponseEntity<APIResponse> deletePaymentMethod(@RequestBody Map<String, Object> payLoad) {
        try {
            String paymentMethodId = payLoad.get(JsonFieldName.PAYMENT_METHOD_ID).toString();

            PaymentMethod paymentMethod =
                    PaymentMethod.retrieve(paymentMethodId);
            paymentMethod.detach();

            APIResponse apiResponse = APIResponse.builder()
                    .responseCode(String.valueOf(HttpStatus.OK.value()))
                    .responseMsg(HttpStatus.OK.getReasonPhrase())
                    .responseData(null)
                    .build();
            return ResponseEntity.ok().body(apiResponse);

        } catch (StripeException e) {
            APIResponse apiResponse = APIResponse.builder()
                    .responseCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                    .responseMsg(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                    .responseData(null)
                    .build();
            return ResponseEntity.ok().body(apiResponse);
        }
    }

    @PostMapping("/CreateSetupIntent")
    public ResponseEntity<APIResponse> createSetupIntent(@RequestBody Map<String, Object> payLoad) {
        //Stripe.apiKey = secretKey;
        try {

            String customerId;
            String email = payLoad.get(JsonFieldName.EMAIL).toString();

            CustomerCollection customerCollection =
                    Customer.list(
                            CustomerListParams
                                    .builder()
                                    .setEmail(email)
                                    .setLimit(1L)
                                    .build()
                    );

            //Checks the if the customer exists, if not creates a new customer
            if (customerCollection.getData().size() != 0) {
                customerId = customerCollection.getData().get(0).getId();

            }
            else {
                Customer customer = Customer.create(CustomerCreateParams
                        .builder()
                        .setEmail(email)
                        .build());
                customerId = customer.getId();
            }

            //Creates a temporary secret key linked with the customer
            EphemeralKey ephemeralKey = EphemeralKey.create(EphemeralKeyCreateParams
                    .builder()
                    .setCustomer(customerId)
                    .setStripeVersion(Stripe.API_VERSION)
                    .build());

            //Creates a new payment intent with amount passed in from the client
            SetupIntent setupIntent = SetupIntent.create(SetupIntentCreateParams
                    .builder()
                    .setCustomer(customerId)
                    .setAutomaticPaymentMethods(SetupIntentCreateParams.AutomaticPaymentMethods
                            .builder()
                            .setEnabled(true)
                            .build())
                    .build());

            logger.info("Create Setup Intent successfully to token: {}", setupIntent.getClientSecret());

            Map<String, Object> response = new HashMap<>();
            response.put(JsonFieldName.CLIENT_SECRET, setupIntent.getClientSecret());
            response.put(JsonFieldName.EPHEMERAL_KEY, ephemeralKey.getId());
            response.put(JsonFieldName.CUSTOMER_ID, customerId);

            APIResponse apiResponse = APIResponse.builder()
                    .responseCode(String.valueOf(HttpStatus.OK.value()))
                    .responseMsg(HttpStatus.OK.getReasonPhrase())
                    .responseData(response)
                    .build();
            return ResponseEntity.ok().body(apiResponse);

        } catch (StripeException e) {
            APIResponse apiResponse = APIResponse.builder()
                    .responseCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                    .responseMsg(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                    .responseData(null)
                    .build();
            return ResponseEntity.ok().body(apiResponse);
        }
    }

    @PostMapping("/CreatePaymentIntentByNewCard")
    public ResponseEntity<APIResponse> createPaymentIntentByNewCard(@RequestBody Map<String, Object> payLoad) {

        try {

            String customerId;
            String email = payLoad.get(JsonFieldName.EMAIL).toString();
            boolean allowFutureUsage = (boolean) payLoad.get(JsonFieldName.ALLOW_FUTURE_USAGE);
            PaymentIntentCreateParams createParams;
            if (allowFutureUsage) {
                CustomerCollection customerCollection =
                        Customer.list(
                                CustomerListParams
                                        .builder()
                                        .setEmail(email)
                                        .setLimit(1L)
                                        .build()
                        );

                //Checks the if the customer exists, if not creates a new customer
                if (customerCollection.getData().size() != 0) {
                    customerId = customerCollection.getData().get(0).getId();

                }
                else {
                    Customer customer = Customer.create(CustomerCreateParams
                            .builder()
                            .setEmail(email)
                            .build());
                    customerId = customer.getId();
                }


                createParams = new
                        PaymentIntentCreateParams.Builder()
                        .setCurrency(payLoad.get(JsonFieldName.CURRENCY).toString())
                        .setAmount(Long.valueOf(payLoad.get(JsonFieldName.AMOUNT).toString()))
                        //.setPaymentMethod("pm_card_visa")
                        .addPaymentMethodType("card")
                        .setCustomer(customerId)
                        .setSetupFutureUsage(allowFutureUsage ?
                                PaymentIntentCreateParams.SetupFutureUsage.ON_SESSION :
                                PaymentIntentCreateParams.SetupFutureUsage.OFF_SESSION)
                        .build();
            }
            else {
                createParams = new
                        PaymentIntentCreateParams.Builder()
                        .setCurrency(payLoad.get(JsonFieldName.CURRENCY).toString())
                        .setAmount(Long.valueOf(payLoad.get(JsonFieldName.AMOUNT).toString()))
                        //.setPaymentMethod("pm_card_visa")
                        .addPaymentMethodType("card")
                        .build();
            }
            PaymentIntent paymentIntent = PaymentIntent.create(createParams);

            logger.info("PaymentController.createPaymentIntentByNewCard clientSecret:{}", paymentIntent.getClientSecret());
            logger.info("PaymentController.createPaymentIntentByNewCard getId:{}", paymentIntent.getId());
            logger.info("PaymentController.createPaymentIntentByNewCard getLatestCharge:{}", paymentIntent.getLatestCharge());
            logger.info("PaymentController.createPaymentIntentByNewCard getInvoice:{}", paymentIntent.getInvoice());
            Map<String, Object> response = new HashMap<>();
            response.put(JsonFieldName.CLIENT_SECRET, paymentIntent.getClientSecret());
            response.put(JsonFieldName.PAYMENT_INTENT_ID, paymentIntent.getId());

            APIResponse apiResponse = APIResponse.builder()
                    .responseCode(String.valueOf(HttpStatus.OK.value()))
                    .responseMsg(HttpStatus.OK.getReasonPhrase())
                    .responseData(response)
                    .build();
            return ResponseEntity.ok().body(apiResponse);

        } catch (StripeException e) {
            APIResponse apiResponse = APIResponse.builder()
                    .responseCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                    .responseMsg(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                    .responseData(null)
                    .build();
            return ResponseEntity.ok().body(apiResponse);
        }
    }

    @RequestMapping(value = "/webhook", method = RequestMethod.POST)
    @Override
    public ResponseEntity<String> webhook(String payload, String sigHeader) {

        Event event = null;
        try {
            event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
        } catch (SignatureVerificationException e) {
            logger.info("Failed signature verification");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
        StripeObject stripeObject = null;

        if (dataObjectDeserializer.getObject().isPresent()) {
            stripeObject = dataObjectDeserializer.getObject().get();
        }
        else {
            // Deserialization failed, probably due to an API version mismatch.
            // Refer to the Javadoc documentation on `EventDataObjectDeserializer` for
            // instructions on how to handle this case, or return an error here.
        }

        // Handle the event
        switch (event.getType()) {
            case "payment_intent.succeeded":
                PaymentIntent paymentIntent = (PaymentIntent) stripeObject;
                logger.info("Payment for " + paymentIntent.getAmount() + " succeeded." + "last charge:" + paymentIntent.getLatestCharge());
                logger.info("PaymentController.webhook getId:{}", paymentIntent.getId());
                logger.info("PaymentController.webhook getLatestCharge:{}", paymentIntent.getLatestCharge());
                logger.info("PaymentController.webhook getInvoice:{}", paymentIntent.getInvoice());
                // Then define and call a method to handle the successful payment intent.
                // handlePaymentIntentSucceeded(paymentIntent);
                break;
            case "payment_method.attached":
                PaymentMethod paymentMethod = (PaymentMethod) stripeObject;
                // Then define and call a method to handle the successful attachment of a PaymentMethod.
                // handlePaymentMethodAttached(paymentMethod);
                logger.info("payment_intent.attached: " + event.getType());
                break;
            case "payment_intent.created":
                //PaymentMethod paymentMethod = (PaymentMethod) stripeObject;
                // Then define and call a method to handle the successful attachment of a PaymentMethod.
                // handlePaymentMethodAttached(paymentMethod);
                logger.info("payment_intent.created: " + event.getType());
                break;
            default:
                logger.info("Unhandled event type: " + event.getType());
                break;
        }
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    @RequestMapping(value = "/Refund", method = RequestMethod.POST)
    @Override
    public ResponseEntity<APIResponse> processRefund(@RequestBody Map<String, Object> payLoad) {
        try {
            //Stripe.apiKey = secretKey;
            String serialNumber = payLoad.get(JsonFieldName.SERIAL_NUMBER).toString();
            String paymentIntentId = payLoad.get(JsonFieldName.PAYMENT_INTENT_ID).toString();
            String amount = payLoad.get(JsonFieldName.AMOUNT).toString();
            /*if (amount != null) {
                amount = amount;
            }*/
            //Find charge from payment intent
            ChargeListParams chargeParams = ChargeListParams.builder()
                    .setPaymentIntent(paymentIntentId)
                    .setLimit(1L)
                    .build();
            ChargeCollection charges = Charge.list(chargeParams);
            int chargeCount = charges.getData().size();
            if (chargeCount == 0) {
                APIResponse apiResponse = APIResponse.builder()
                        .responseCode(String.valueOf(HttpStatus.NOT_FOUND.value()))
                        .responseMsg("No charge found")
                        .responseData("")
                        .build();
                return ResponseEntity.ok().body(apiResponse);
            }
            Charge data = charges.getData().get(0);


           /* long amountL = 0L;
            if (amount.isEmpty()) {
                //treat as full refund
                amountL = data.getAmount();
            }
            else {
                amountL = Long.parseLong(amount) * 100;
                if (amountL > data.getAmount()) {
                    APIResponse apiResponse = APIResponse.builder()
                            .responseCode(String.valueOf(HttpStatus.BAD_REQUEST.value()))
                            .responseMsg("Amount more than purchase value")
                            .responseData("")
                            .build();
                    return ResponseEntity.ok().body(apiResponse);
                }
            }*/

            Optional<TicketMaster>  ticketMaster = ticketService.findBySerialNumber(serialNumber);
            if(ticketMaster.isEmpty()){
                APIResponse apiResponse = APIResponse.builder()
                        .responseCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                        .responseMsg(HttpStatus.NOT_FOUND.getReasonPhrase())
                        .responseData(null)
                        .build();
                return ResponseEntity.ok().body(apiResponse);
            }

            // Create policy
            RefundPolicyService refundPolicyService = refundPolicyServiceFactoryImpl.createRefundPolicy(RefundPolicyTypeEnum.PARTIAL_REFUND);
            if(refundPolicyServiceFactoryImpl.hoursDifference(
                    ticketMaster.get().getEffectiveDateTime()) <=24){
                refundPolicyService = refundPolicyServiceFactoryImpl.createRefundPolicy(RefundPolicyTypeEnum.FULL_REFUND);
            }
            long refundAmount = refundPolicyService.calculateRefund(ticketMaster.get());

            //amount integer, charge string, payment_intent string, reason string
            RefundCreateParams refundParams =
                    RefundCreateParams.builder()
                            .setCharge(data.getId())
                            .setAmount(refundAmount)
                            .setReason(RefundCreateParams.Reason.REQUESTED_BY_CUSTOMER)
                            .build();
            Refund refundResponse = Refund.create(refundParams);
            logger.info(String.valueOf(refundResponse));

            double refundAmt = ((double) refundResponse.getAmount()) / 100.0;
            ticketService.updateRefund(serialNumber, TicketStatusEnum.REFUND_APPROVED);

            APIResponse apiResponse = APIResponse.builder()
                    .responseCode(String.valueOf(HttpStatus.OK.value()))
                    .responseMsg(HttpStatus.OK.getReasonPhrase())
                    .responseData(refundAmt + " " + refundResponse.getCurrency() + " refunded successfully")
                    .build();
            return ResponseEntity.ok().body(apiResponse);

        } catch (StripeException e) {
            String errorMsg = HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase();
            if (e.getCode() != null && e.getCode().equals("charge_already_refunded")) {
                errorMsg = "charge already refunded";
            }
            logger.info(e.getMessage());
            APIResponse apiResponse = APIResponse.builder()
                    .responseCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                    .responseMsg(errorMsg)
                    .responseData(null)
                    .build();
            return ResponseEntity.ok().body(apiResponse);
        }
    }

}
