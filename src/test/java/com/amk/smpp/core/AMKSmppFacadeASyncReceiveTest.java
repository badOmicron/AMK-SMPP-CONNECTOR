
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
import org.smpp.TCPIPConnection;
import org.smpp.pdu.Address;
import org.smpp.pdu.DeliverSM;
import org.smpp.smscsim.SimulatorPDUProcessor;

import com.amk.smpp.operation.PDUOperation;
import com.amk.smpp.operation.PDUOperationProperties;
import com.amk.smpp.operation.PDUOperationPropertiesBuilder;
import com.amk.smpp.operation.PDUOperationTypes;
import com.amk.smpp.sim.SIMSimulator;
import com.amk.smpp.util.BasicPDUListener;

/**
 * Test
 * @author Orlando Ramos &lt;orlando.ramos@amk-technologies.com&gt;
 * @version 1.0.0
 * @since 1.0.0
 */
@PowerMockIgnore("javax.management.*")
@RunWith(PowerMockRunner.class)
@PrepareForTest({})
public class AMKSmppFacadeASyncReceiveTest {
    /**
     * Logger for class.
     */
    private static final Logger LOGGER = LogManager.getLogger(AMKSmppFacadeASyncReceiveTest.class);

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
        Thread.currentThread().setName("AMKSmppFacadeASyncReceiveTest");
    }

    /**
     * Se configura y se arranca el Simulador.
     * @throws Exception .
     */
    private void setUpSimulator() throws Exception {
        if (Objects.isNull(simSimulator)) {
            simSimulator = new SIMSimulator();
            simSimulator.stop();
            simSimulator.init(3101);
        }

    }

    /**
     * Configura la Conexión con el SMCS.
     * @throws Exception .
     */
    private void setUpConnection() throws Exception {
        connection = new TCPIPConnection("0.0.0.0", 3101);
        smppFacade = new AMKSmppFacade(connection);
        bindingManager = new BindingManager("hugo", "ggoohu", connection);
        smppFacade.setBindingManager(bindingManager);
    }

    @Test
    public void executeOperation() throws Exception {
        setUpSimulator();
        receive();
    }

    private void receive() throws Exception {
        // Reciviendo mensaje.
        PDUOperationProperties props = new PDUOperationPropertiesBuilder()
                .setSourceAddress(new Address("5529094190"))
                .setDestAddress(new Address[]{new Address("5529094190")})
                .build();

        BasicPDUListener listener = new BasicPDUListener();
        listener.setIntervalTime(1000);

        PDUOperation receive = PDUOperation
                .newBuilder()
                .withOperationProps(props)
                .withOperationType(PDUOperationTypes.RECEIVE)
                .withAsynchronous(true)
                .withListener(listener)
                .withBindingType(BindingType.TRX)
                .build();

        Thread thread = new Thread(sendMessageToESME());
        thread.setName("SCMS SIMULATOR");
        thread.setDaemon(true);
        thread.start();

        try {
            smppFacade.executeOperation(receive);
            Assert.fail();
        } catch (final Exception e) {
            // error expected
        }

        DeliverSM deliverSM = smppFacade.receiveOperation(receive);
        LOGGER.debug("getShortMessage: " + deliverSM.getShortMessage());
    }


    public synchronized Runnable sendMessageToESME() {
        return new Runnable() {
            int procCount;
            SimulatorPDUProcessor proc;

            /**
             * When an object implementing interface <code>Runnable</code> is used
             * to create a thread, starting the thread causes the object's
             * <code>run</code> method to be called in that separately executing
             * thread.
             * <p>
             * The general contract of the method <code>run</code> is that it may
             * take any action whatsoever.
             *
             * @see     Thread#run()
             */
            @Override
            public void run() {
                while (simSimulator.processors.count() <= 0) {
                    long waitTime = 5000;
                    try {
                        LOGGER.warn("...esperando " + waitTime / 1000 + " segs");
                        Thread.sleep(waitTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    procCount = simSimulator.processors.count();
                    for (int i = 0; i < simSimulator.processors.count(); i++) {
                        proc = (SimulatorPDUProcessor) simSimulator.processors.get(i);
                        if (proc.isActive()) {
                            String message = "ESTE MENSAJE ES ENVIADO DE LA OPERADORA ASYN";
                            DeliverSM request = new DeliverSM();
                            try {
                                request.setShortMessage(message);
                                proc.serverRequest(request);
                                LOGGER.debug("Message sent.");
                            } catch (Exception e) {
                                LOGGER.error("Async Message sending failed");
                            }
                        } else {
                            LOGGER.warn("This session is inactive.");
                        }
                    }
                }
            }
        };
    }
}