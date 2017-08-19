package com.amk.smpp.core;

import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.smpp.Connection;
import org.smpp.ServerPDUEvent;
import org.smpp.TCPIPConnection;
import org.smpp.pdu.Address;
import org.smpp.pdu.CancelSMResp;
import org.smpp.pdu.DataSMResp;
import org.smpp.pdu.EnquireLinkResp;
import org.smpp.pdu.QuerySMResp;
import org.smpp.pdu.ReplaceSMResp;
import org.smpp.pdu.SubmitMultiSMResp;
import org.smpp.pdu.SubmitSMResp;

import com.amk.smpp.operation.PDUOperation;
import com.amk.smpp.operation.PDUOperationProperties;
import com.amk.smpp.operation.PDUOperationPropertiesBuilder;
import com.amk.smpp.operation.PDUOperationTypes;
import com.amk.smpp.sim.SIMSimulator;
import com.amk.smpp.util.BasicPDUListener;
import com.amk.smpp.util.Message;

/**
 * Test
 * @author Orlando Ramos &lt;orlando.ramos@amk-technologies.com&gt;
 * @version 1.0.0
 * @since 1.0.0
 */
@PowerMockIgnore("javax.management.*")
@RunWith(PowerMockRunner.class)
@PrepareForTest({})
public class AMKSmppFacadeSyncSubmitTest {
    /**
     * Logger for class.
     */
    private static final Logger LOGGER = LogManager.getLogger(AMKSmppFacadeSyncSubmitTest.class);

    private SIMSimulator   simSimulator;
    /**
     * SMPP Connection
     */
    private Connection     connection;
    private AMKSmppFacade  smppFacade;
    private BindingManager bindingManager;

    /**
     * Configuraciones varias.
     * @throws Exception .
     */
    @Before
    public void setUp() throws Exception {
        setUpThread();
        setUpConnection();
    }

    /**
     * Configuración del Thread.
     */
    private void setUpThread() {
        Thread.currentThread().setName("AMKSmppFacadeSyncSubmitTest");
    }

    /**
     * Se configura y se arranca el Simulador.
     * @throws Exception .
     */
    private void setUpSimulator() throws Exception {
        if (Objects.isNull(simSimulator)) {
            simSimulator = new SIMSimulator();
            simSimulator.stop();
            simSimulator.init(2300);
        }

    }

    /**
     * Configura la Conexión con el SMCS.
     * @throws Exception .
     */
    private void setUpConnection() throws Exception {
        connection = new TCPIPConnection("0.0.0.0", 2300);
        smppFacade = new AMKSmppFacade(connection);
        bindingManager = new BindingManager("hugo", "ggoohu", connection);
        smppFacade.setBindingManager(bindingManager);
    }

    @Test
    public void AMKSmppFacade() throws Exception {
        try {
            new AMKSmppFacade(null);
            Assert.fail();
        } catch (final Exception e) {
            // error expected
        }
        AMKSmppFacade smppFacade = new AMKSmppFacade(connection);
        Assert.assertNotNull(smppFacade);
    }

    @Test
    public void executeOperation() throws Exception {
        setUpSimulator();
        submit();
        data();
        multi();
    }

    private void submit() throws Exception {
        LOGGER.debug("executeOperation: " + PDUOperationTypes.SUBMIT_SMS);
        Assert.assertNotNull(connection);
        Assert.assertNotNull(smppFacade);
        PDUOperationProperties props = new PDUOperationPropertiesBuilder()
                .setSourceAddress(new Address("5529094190"))
                .setDestAddress(new Address[]{new Address("5529094190")})
                .build();
        Message smsMessage = new Message();
        smsMessage.setBody("Mi mensaje de prueba");
        PDUOperation submit = PDUOperation
                .newBuilder()
                .withOperationProps(props)
                .withOperationType(PDUOperationTypes.SUBMIT_SMS)
                .withAsynchronous(false)
                .withSmsMessage(smsMessage)
                .withBindingType(BindingType.TRX)
                .build();
        SubmitSMResp response = smppFacade.executeOperation(submit);
        String messageId = response.getMessageId();
        simSimulator.messageStore.print();
        submit.getSmsMessage().setId(messageId);
        Assert.assertNotNull(simSimulator.messageStore.getMessage(messageId));
        query(messageId);
        replace(messageId);
    }

    private void data() throws Exception {
        LOGGER.debug("executeOperation: " + PDUOperationTypes.DATA);
        Assert.assertNotNull(smppFacade);
        // Enviar mensaje sincrono --> Se espera la respuesta.
        PDUOperationProperties props = new PDUOperationPropertiesBuilder()
                .setSourceAddress(new Address("5529094190"))
                .setDestAddress(new Address[]{new Address("5529094190")})
                .build();
        Message smsMessage = new Message();
        smsMessage.setBody("Mi mensaje de prueba Data");
        PDUOperation data = PDUOperation
                .newBuilder()
                .withOperationProps(props)
                .withOperationType(PDUOperationTypes.DATA)
                .withAsynchronous(false)
                .withSmsMessage(smsMessage)
                .withBindingType(BindingType.TRX)
                .build();
        DataSMResp response = smppFacade.executeOperation(data);
        String messageId = response.getMessageId();
        Assert.assertNotNull(messageId);
        simSimulator.messageStore.print();
    }


    private void cancel(PDUOperation pduOperation) throws Exception {
        LOGGER.debug("cancel: TEST");
        pduOperation.setOperationType(PDUOperationTypes.CANCEL);
        CancelSMResp response = smppFacade.executeOperation(pduOperation);
        response.debugString();
        simSimulator.messageStore.print();
        Assert.assertNull(simSimulator.messageStore.getMessage(pduOperation.getSmsMessage().getId()));
    }

    private void multi() throws Exception {
        Assert.assertNotNull(connection);
        Assert.assertNotNull(smppFacade);
        // Enviar mensaje sincrono --> Se espera la respuesta.
        PDUOperationProperties props = new PDUOperationPropertiesBuilder()
                .setSourceAddress(new Address("5529094190"))
                .setDestAddress(new Address[]{new Address("5529094190"), new Address("6778730688")})
                .build();
        Message smsMessage = new Message();
        smsMessage.setBody("Mi mensaje de prueba MULTI");
        PDUOperation multiSubmit = PDUOperation
                .newBuilder()
                .withOperationProps(props)
                .withOperationType(PDUOperationTypes.SUBMIT_SMS_MULTI)
                .withAsynchronous(false)
                .withSmsMessage(smsMessage)
                .withBindingType(BindingType.TRX)
                .build();
        SubmitMultiSMResp response = smppFacade.executeOperation(multiSubmit);
        String messageId = response.getMessageId();
        Assert.assertNotNull(messageId);
    }

    private void query(String messageId) throws Exception {
        PDUOperationProperties props = new PDUOperationPropertiesBuilder()
                .setSourceAddress(new Address("5529094190"))
                .setDestAddress(new Address[]{new Address("5529094190")})
                .build();
        Message smsMessage = new Message();
        smsMessage.setId(messageId);
        smsMessage.setBody("Mi mensaje de prueba QUERY");
        PDUOperation data = PDUOperation
                .newBuilder()
                .withOperationProps(props)
                .withOperationType(PDUOperationTypes.QUERY)
                .withAsynchronous(false)
                .withSmsMessage(smsMessage)
                .withBindingType(BindingType.TRX)
                .build();
        QuerySMResp queryResponse = smppFacade.executeOperation(data);
        queryResponse.debugString();
        Assert.assertNotNull(queryResponse.getBody());
    }

    private void replace(String messageId) throws Exception {
        PDUOperationProperties props = new PDUOperationPropertiesBuilder()
                .setSourceAddress(new Address("5529094190"))
                .setDestAddress(new Address[]{new Address("5529094190")})
                .build();
        Message smsMessage = new Message();
        smsMessage.setId(messageId);
        smsMessage.setBody("Mi mensaje de prueba REPLACE");
        PDUOperation replace = PDUOperation
                .newBuilder()
                .withOperationProps(props)
                .withOperationType(PDUOperationTypes.REPLACE)
                .withAsynchronous(false)
                .withSmsMessage(smsMessage)
                .withBindingType(BindingType.TRX)
                .build();
        ReplaceSMResp response = smppFacade.executeOperation(replace);
        Assert.assertNotNull(response);
        simSimulator.messageStore.print();
        cancel(replace);
    }

    private void enquire() throws Exception {
        LOGGER.debug("executeOperation: " + PDUOperationTypes.ENQUIRE);
        Assert.assertNotNull(connection);
        Assert.assertNotNull(smppFacade);
        PDUOperationProperties props = new PDUOperationPropertiesBuilder()
                .setSourceAddress(new Address("5529094190"))
                .setDestAddress(new Address[]{new Address("5529094190")})
                .build();
        PDUOperation enquire = PDUOperation
                .newBuilder()
                .withOperationProps(props)
                .withOperationType(PDUOperationTypes.ENQUIRE)
                .withAsynchronous(true)
                .withSmsMessage(new Message())
                .withBindingType(BindingType.TRX)
                .build();
        BasicPDUListener listener = new BasicPDUListener();
        listener.setIntervalTime(1000);
        enquire.setListener(listener);
        smppFacade.executeOperation(enquire);
        ServerPDUEvent event = listener.getResponseEvent();
        Assert.assertNotNull(event);
        EnquireLinkResp response = new EnquireLinkResp();
        response.setBody(event.getPDU().getBody());
        Assert.assertNotNull(response.getBody());

    }
}