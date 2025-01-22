package com.qre.tg.query.api.controller.impl;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.qre.tg.dto.base.APIResponse;
import com.qre.tg.dto.base.JsonFieldName;
import com.qre.tg.entity.ticket.TicketMaster;
import com.qre.tg.query.api.common.RefundPolicyTypeEnum;
import com.qre.tg.query.api.service.RefundPolicyService;
import com.qre.tg.query.api.service.TicketService;
import com.qre.tg.query.api.service.impl.FullRefundPolicyServiceImpl;
import com.qre.tg.query.api.service.impl.RefundPolicyServiceFactoryImpl;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class StripePaymentControllerImplTest {

    @InjectMocks
    private StripePaymentControllerImpl controller;

    @Mock
    private PaymentIntent paymentIntentMock;

    @Mock
    private TicketService ticketService;

    @Mock
    private RefundPolicyServiceFactoryImpl refundPolicyServiceFactoryImpl;

    @Mock
    RefundPolicyService refundPolicyService;

    @BeforeEach
    void setUp() {
        Stripe.apiKey = "sk_test_51O42D0Fcp66ilBOoao2sX9sgGzB8reIIayTsn7MmLIDe94IaHQjNRvEtLXN0i6WZphQlCh7X9UVkskKlNDJGOM2y00jxbDVEaV";
    }

    /**
     * This test using mock framework
     * @throws StripeException
     */
    @Test
    public void createPaymentIntent_success() throws StripeException {

        // Set up mock data
        Map<String, Object> payload = new HashMap<>();
        payload.put(JsonFieldName.EMAIL, "test@example.com");
        payload.put(JsonFieldName.ALLOW_FUTURE_USAGE, true);
        payload.put(JsonFieldName.CURRENCY, "sgd");
        payload.put(JsonFieldName.AMOUNT, 1000);

        MockedStatic<PaymentIntent> paymentIntentStaticMock = Mockito.mockStatic(PaymentIntent.class);

        // Configure the mock behavior for PaymentIntent.create
        paymentIntentStaticMock.when(() -> PaymentIntent.create(any(PaymentIntentCreateParams.class)))
                .thenReturn(paymentIntentMock); // Return your mocked paymentIntentMock

        when(paymentIntentMock.getClientSecret()).thenReturn("pi_client_secret");
        when(paymentIntentMock.getId()).thenReturn("pi_123");

        // Call the method
        ResponseEntity<APIResponse> response = controller.createPaymentIntent(payload);

        Map<String, Object>  data = (Map<String, Object>) response.getBody().getResponseData();

        // Assert results
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("pi_client_secret", data.get(JsonFieldName.CLIENT_SECRET));
        assertEquals("pi_123", data.get(JsonFieldName.PAYMENT_INTENT_ID));

        // **Manual Cleanup**
        paymentIntentStaticMock.close(); // Remember to close the mock manually

    }

    /**
     * Test directly with stripe
     * @throws StripeException
     */
    @Test
    void createPaymentIntentTest_success() throws StripeException {
        // Mocking input payload
        Map<String, Object> payload = new HashMap<>();
        payload.put(JsonFieldName.EMAIL, "test@example.com");
        payload.put(JsonFieldName.ALLOW_FUTURE_USAGE, true);
        payload.put(JsonFieldName.CURRENCY, "sgd");
        payload.put(JsonFieldName.AMOUNT, 200);

        // Calling the controller method
        ResponseEntity<APIResponse> response = controller.createPaymentIntent(payload);

        Map<String, Object> responseData = (Map<String, Object>) response.getBody().getResponseData();
        // Verifying the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody(), "Response body should not be null");
        assertNotEquals("", responseData.get(JsonFieldName.CLIENT_SECRET), "client id should not be empty");
        assertNotEquals("", JsonFieldName.PAYMENT_INTENT_ID, "payment intent id should not be empty");
    }


    /**
     * Test directly with stripe
     * @throws StripeException
     */
    @Test
    void fetchCustomerCards_success() throws StripeException {
        // Mocking input payload
        Map<String, Object> payload = new HashMap<>();
        payload.put(JsonFieldName.EMAIL, "test@example.com");

        // Calling the controller method
        ResponseEntity<APIResponse> response = controller.fetchCustomerCards(payload);

        // Verifying the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody(), "Response body should not be null");
    }

    /**
     * Test directly with stripe
     * @throws StripeException
     */
    @Test
    void deletePaymentMethodTest_success() throws StripeException {
        // Mocking input payload
        Map<String, Object> payload = new HashMap<>();
        payload.put(JsonFieldName.PAYMENT_METHOD_ID, "p1234");

        // Calling the controller method
        ResponseEntity<APIResponse> response = controller.deletePaymentMethod(payload);
        // Verifying the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody(), "Response body should not be null");
    }

    @Test
    void createSetupIntentTest_success() throws StripeException {
        // Mocking input payload
        Map<String, Object> payload = new HashMap<>();
        payload.put(JsonFieldName.EMAIL, "test@example.com");

        // Calling the controller method
        ResponseEntity<APIResponse> response = controller.createSetupIntent(payload);

        Map<String, Object> responseData = (Map<String, Object>) response.getBody().getResponseData();
        // Verifying the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody(), "Response body should not be null");
        assertNotEquals("", responseData.get(JsonFieldName.CLIENT_SECRET), "client id should not be empty");
        assertNotEquals("", JsonFieldName.EPHEMERAL_KEY, "ephemeral key should not be empty");
        assertNotEquals("", JsonFieldName.CUSTOMER_ID, "client should not be empty");
    }

    /**
     * Test directly with stripe
     * @throws StripeException
     */
    @Test
    void createPaymentIntentByNewCardTest_success() throws StripeException {
        // Mocking input payload
        Map<String, Object> payload = new HashMap<>();
        payload.put(JsonFieldName.EMAIL, "test@example.com");
        payload.put(JsonFieldName.ALLOW_FUTURE_USAGE, true);
        payload.put(JsonFieldName.CURRENCY, "sgd");
        payload.put(JsonFieldName.AMOUNT, 200);

        // Calling the controller method
        ResponseEntity<APIResponse> response = controller.createPaymentIntentByNewCard(payload);

        Map<String, Object> responseData = (Map<String, Object>) response.getBody().getResponseData();
        // Verifying the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody(), "Response body should not be null");
        assertNotEquals("", responseData.get(JsonFieldName.CLIENT_SECRET), "client id should not be empty");
        assertNotEquals("", JsonFieldName.PAYMENT_INTENT_ID, "payment intent id should not be empty");
    }


    /**
     * Test directly with stripe
     * @throws StripeException
     */
    @Test
    void processRefundTest_success() throws StripeException {
        // Mocking input payload
        Map<String, Object> payload = new HashMap<>();
        payload.put(JsonFieldName.PAYMENT_INTENT_ID, "pi_3PAW6TFcp66ilBOo0eJLEf4q");
        payload.put(JsonFieldName.SERIAL_NUMBER, "2024042819413710WEB0000000000001");
        payload.put(JsonFieldName.AMOUNT, 200);

        TicketMaster ticketMaster = TicketMaster.builder()
                .serialNumber("2024042819413710WEB0000000000001")
                .build();
        when(ticketService.findBySerialNumber(anyString())).thenReturn(Optional.of(ticketMaster));
        when(refundPolicyServiceFactoryImpl.createRefundPolicy(any())).thenReturn(refundPolicyService);

        when(refundPolicyService.calculateRefund(any())).thenReturn(100L);


        // Calling the controller method
        ResponseEntity<APIResponse> response = controller.processRefund(payload);

        // Verifying the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody(), "Response body should not be null");
    }
}
