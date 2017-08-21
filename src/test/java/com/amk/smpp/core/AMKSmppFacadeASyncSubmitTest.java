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
public class AMKSmppFacadeASyncSubmitTest {
    /**
     * Logger for class.
     */
    private static final Logger LOGGER = LogManager.getLogger(AMKSmppFacadeASyncSubmitTest.class);

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
    }

    /**
     * Configuración del Thread.
     */
    private void setUpThread() {
        Thread.currentThread().setName("AMKSmppFacadeASyncSubmitTest");
    }

    /**
     * Se configura y se arranca el Simulador.
     * @throws Exception .
     */
    private void setUpSimulator(int port) throws Exception {
        if (Objects.isNull(simSimulator)) {
            simSimulator = new SIMSimulator();
            simSimulator.stop();
            simSimulator.init(port);
        }

    }

    /**
     * Configura la Conexión con el SMCS.
     * @throws Exception .
     */
    private void setUpConnection(int port) throws Exception {
        connection = new TCPIPConnection("0.0.0.0", port);
        smppFacade = new AMKSmppFacade(connection);
        bindingManager = new BindingManager("hugo", "ggoohu", connection);
        smppFacade.setBindingManager(bindingManager);
    }

    @Test
    public void AMKSmppFacade() throws Exception {
        setUpConnection(21000);
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
        setUpSimulator(24000);
        setUpConnection(24000);
        submit();
    }

    @Test
    public void executeOperationData() throws Exception {
        setUpSimulator(25000);
        setUpConnection(25000);
        data();
    }

    @Test
    public void executeOperationCancel() throws Exception {
        setUpSimulator(26000);
        setUpConnection(26000);
        cancel();
    }

    @Test
    public void executeOperationMulti() throws Exception {
        setUpSimulator(28000);
        setUpConnection(28000);
        multi();
    }

    @Test
    public void executeOperationQuery() throws Exception {
        setUpSimulator(29000);
        setUpConnection(29000);
        query();
    }

    @Test
    public void executeOperationReplace() throws Exception {
        setUpSimulator(30000);
        setUpConnection(30000);
        replace();
    }

    @Test
    public void executeOperationEnquire() throws Exception {
        setUpSimulator(31000);
        setUpConnection(31000);
        enquire();
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
                .withAsynchronous(true)
                .withSmsMessage(smsMessage)
                .withBindingType(BindingType.TRX)
                .build();
        BasicPDUListener listener = new BasicPDUListener();
        listener.setIntervalTime(1000);
        submit.setListener(listener);
        smppFacade.executeOperation(submit);
        ServerPDUEvent event = listener.getResponseEvent();
        Assert.assertNotNull(event);
        SubmitSMResp response = new SubmitSMResp();
        response.setBody(event.getPDU().getBody());
        String messageId = response.getMessageId();
        Assert.assertNotNull(messageId);
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
                .withAsynchronous(true)
                .withSmsMessage(smsMessage)
                .withBindingType(BindingType.TRX)
                .build();
        BasicPDUListener listener = new BasicPDUListener();
        listener.setIntervalTime(2000);
        data.setListener(listener);
        smppFacade.executeOperation(data);
        ServerPDUEvent event = listener.getResponseEvent();
        Assert.assertNotNull(event);
        Assert.assertTrue(event.getReceiver().isReceiver());
        DataSMResp response = new DataSMResp();
        response.setData(event.getPDU().getData());
        String messageId = response.getMessageId();
        Assert.assertNotNull(messageId);
    }

    private void cancel() throws Exception {
        LOGGER.debug("cancel: TEST");
        PDUOperationProperties props = new PDUOperationPropertiesBuilder()
                .setSourceAddress(new Address("5529094190"))
                .setDestAddress(new Address[]{new Address("5529094190")})
                .build();
        Message smsMessage = new Message();
        smsMessage.setBody("Mi mensaje de prueba cancel");
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
        smsMessage.setId(messageId);
        bindingManager.unBind();
        PDUOperation cancel = PDUOperation
                .newBuilder()
                .withOperationProps(props)
                .withOperationType(PDUOperationTypes.CANCEL)
                .withAsynchronous(true)
                .withSmsMessage(smsMessage)
                .withBindingType(BindingType.TRX)
                .build();

        BasicPDUListener listener = new BasicPDUListener();
        listener.setIntervalTime(2000);
        cancel.setListener(listener);
//        CancelSMResp cancelResponse =
        smppFacade.executeOperation(cancel);
        // TODO El servidor responde pero el Listener no responde al evento
//        listener.getResponseEvent();
//        ServerPDUEvent event = pduOperation.getListener().getRequestEvent();
//        Assert.assertNotNull(event);
//        CancelSMResp response = new CancelSMResp();
//        response.setBody(event.getPDU().getBody());
//        System.out.println("CANCEL RESPONSE " + response.debugString());
//        simSimulator.messageStore.print();
//        Assert.assertNull(simSimulator.messageStore.getMessage(pduOperation.getSmsMessage().getId()));
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
                .withAsynchronous(true)
                .withSmsMessage(smsMessage)
                .withBindingType(BindingType.TRX)
                .build();
        BasicPDUListener listener = new BasicPDUListener();
        listener.setIntervalTime(2000);
        multiSubmit.setListener(listener);
        smppFacade.executeOperation(multiSubmit);
        ServerPDUEvent event = listener.getResponseEvent();
        SubmitMultiSMResp response = new SubmitMultiSMResp();
        response.setBody(event.getPDU().getBody());
        String messageId = response.getMessageId();
        System.out.println(messageId);
        Assert.assertNotNull(messageId);
    }

    private void query() throws Exception {
        LOGGER.debug("QUERY");
        PDUOperationProperties props = new PDUOperationPropertiesBuilder()
                .setSourceAddress(new Address("5529094190"))
                .setDestAddress(new Address[]{new Address("5529094190")})
                .build();
        Message smsMessage = new Message();
        smsMessage.setBody("Mi mensaje de prueba query");
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
        smsMessage.setId(messageId);
        smsMessage.setBody("Mi mensaje de prueba QUERY");

        bindingManager.unBind();

        PDUOperation query = PDUOperation
                .newBuilder()
                .withOperationProps(props)
                .withOperationType(PDUOperationTypes.QUERY)
                .withAsynchronous(true)
                .withSmsMessage(smsMessage)
                .withBindingType(BindingType.TRX)
                .build();
        BasicPDUListener listener = new BasicPDUListener();
        listener.setIntervalTime(2000);
        query.setListener(listener);
        smppFacade.executeOperation(query);
        ServerPDUEvent event = listener.getResponseEvent();
        QuerySMResp queryResponse = new QuerySMResp();
        queryResponse.setBody(event.getPDU().getBody());
        Assert.assertNotNull(queryResponse.getMessageId());
    }

    private void replace() throws Exception {
        LOGGER.debug("REPLACE");
        PDUOperationProperties props = new PDUOperationPropertiesBuilder()
                .setSourceAddress(new Address("5529094190"))
                .setDestAddress(new Address[]{new Address("5529094190")})
                .build();
        Message smsMessage = new Message();
        smsMessage.setBody("Mi mensaje de prueba replace");
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

        smsMessage.setId(messageId);
        smsMessage.setBody("Mi mensaje de prueba REPLACE");

        bindingManager.unBind();

        PDUOperation replace = PDUOperation
                .newBuilder()
                .withOperationProps(props)
                .withOperationType(PDUOperationTypes.REPLACE)
                .withAsynchronous(true)
                .withSmsMessage(smsMessage)
                .withBindingType(BindingType.TRX)
                .build();
        BasicPDUListener listener = new BasicPDUListener();
        listener.setIntervalTime(2000);
        replace.setListener(listener);
        smppFacade.executeOperation(replace);
        ServerPDUEvent event = listener.getResponseEvent();
        Assert.assertNotNull(event);
        ReplaceSMResp responseReplace = new ReplaceSMResp();
        responseReplace.setBody(event.getPDU().getBody());
        simSimulator.messageStore.print();
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
        Assert.assertNotNull(response);

    }
}