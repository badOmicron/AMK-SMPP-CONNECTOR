package com.amk.smpp;

import java.io.IOException;
import java.io.InputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.smpp.Connection;
import org.smpp.TCPIPConnection;
import org.smpp.pdu.Address;
import org.smpp.smscsim.DeliveryInfoSender;
import org.smpp.smscsim.PDUProcessorGroup;
import org.smpp.smscsim.SMSCListenerImpl;
import org.smpp.smscsim.SMSCSession;
import org.smpp.smscsim.ShortMessageStore;
import org.smpp.smscsim.SimulatorPDUProcessor;
import org.smpp.smscsim.SimulatorPDUProcessorFactory;
import org.smpp.smscsim.util.Table;

/**
 * Test
 * @author Orlando Ramos &lt;orlando.ramos@amk-technologies.com&gt;
 * @version 1.0.0
 * @since 1.0.0
 */
@PowerMockIgnore("javax.management.*")
@RunWith(PowerMockRunner.class)
@PrepareForTest({})
public class AMKSmppFacadeTest {
    /**
     * Logger for class.
     */
    private static final Logger LOGGER = LogManager.getLogger(AMKSmppFacade.class.getName());

    /**
     * Listener que ayuda a simular SMS.
     */
    private SMSCListenerImpl             smscListener;
    /**
     * Contenedor de procesos.
     */
    private PDUProcessorGroup            processors;
    /**
     * Contenedor de SMS's que ha enviado el cliente.
     */
    private ShortMessageStore            messageStore;
    /**
     * Información del envío.
     */
    private DeliveryInfoSender           deliveryInfoSender;
    /**
     * Representa una tabla con los registros de los usuarios.
     */
    private Table                        users;
    /**
     * Fábrica de SimulatorPDUProcessor.
     */
    private SimulatorPDUProcessorFactory factory;

    private String usersFileName = "users.txt";

    /**
     * SMPP Connection
     */
    private Connection connection;
    private String host = "0.0.0.0";
    private int    port = 2300;

    /**
     * Se configura y se arranca el Simulador.
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        setUpThread();
        setUpSimulator();
        setUpConnection();
    }

    private void setUpThread() {
        Thread.currentThread().setName("AMKSmppFacadeTest");
    }

    private void setUpSimulator() throws Exception {
        LOGGER.debug("setUp: SMCS Simluator");
        // Puerto para el Simulador de SMCS.
        int port = 2300;
        smscListener = new SMSCListenerImpl(port, true);
        processors = new PDUProcessorGroup();
        messageStore = new ShortMessageStore();
        deliveryInfoSender = new DeliveryInfoSender();
        LOGGER.debug("setUp: init deliveryInfoSender");
        deliveryInfoSender.start();
        LOGGER.debug("setUp: users");
        InputStream input = this.getClass().getClassLoader().getResourceAsStream(usersFileName);
        users = new Table();
        users.getParser().parse(input);
        LOGGER.debug("setUp: factory Simulator");
        factory = new SimulatorPDUProcessorFactory(processors, messageStore, deliveryInfoSender, users);
        factory.setDisplayInfo(true);
        smscListener.setPDUProcessorFactory(factory);
        LOGGER.debug("setUp: smscListener");
        smscListener.start();
        LOGGER.debug("Simulator Started..");
    }


    private void setUpConnection() throws Exception {
        connection = new TCPIPConnection(host, port);
    }
D
    @Test
    public void AMKSmppFacade() throws Exception {
        try {
            AMKSmppFacade smppFacade = new AMKSmppFacade(null);
            Assert.fail();
        } catch (final Exception e) {
            // error expected
        }
        AMKSmppFacade smppFacade = new AMKSmppFacade(connection);
        Assert.assertNotNull(connection);
    }

    @Test
    public void executeOperation() throws Exception {
        LOGGER.debug("executeOperation: ");
        Assert.assertNotNull(connection);
        AMKSmppFacade smppFacade = new AMKSmppFacade(connection);
        Assert.assertNotNull(smppFacade);
        org.smpp.pdu.WrongDateFormatException w;
        // Enviar mensaje sincrono --> Experamos la respuesta
        PDUOperation submit = new PDUOperation();
        PDUOperationProperties props = new PDUOperationPropertiesBuilder()
                .setSourceAddress(new Address("5529094190"))
                .setDestAddress(new Address[]{new Address("5529094190")})
                .build();
        submit.setOperationProps(props);
        submit.setOperationType(PDUOperationTypes.SUBMIT_SMS);
        submit.setAsynchronous(false);
        submit.setSmsMessage("Mi mensaje de prueba");
        submit.setMessageId("MP01");
        submit.setBindType(SmppBindTypes.TRX);
        smppFacade.executeOperation(submit);

    }

    @After
    public void tearDown() throws Exception {
        stop();
    }

    private void stop() throws IOException {
        LOGGER.debug("stop Simulator ");
        if (smscListener != null) {
            System.out.println("Stopping listener...");
//            synchronized (processors) {
            int procCount = processors.count();
            System.out.println("...process " + procCount);
            SimulatorPDUProcessor proc;
            SMSCSession session;
            for (int i = 0; i < procCount; i++) {
                proc = (SimulatorPDUProcessor) processors.get(i);
                session = proc.getSession();
                LOGGER.debug("Stopping session " + i + ": " + proc.getSystemId() + " ...");
                session.stop();
                LOGGER.debug(" stopped.");
            }
//            }
            LOGGER.debug("... stoping smscListener ");
            smscListener.stop();
            LOGGER.debug("...  smscListener stoped");
            smscListener = null;
            LOGGER.debug("... stoping deliveryInfoSender ");
            if (deliveryInfoSender != null) {
                deliveryInfoSender.stop();
                LOGGER.debug("...  deliveryInfoSender stoped");
            }
            LOGGER.debug("SMCS Simulator Stopped.");
        }
    }

}