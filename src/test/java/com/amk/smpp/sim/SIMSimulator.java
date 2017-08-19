package com.amk.smpp.sim;

import java.io.IOException;
import java.io.InputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.smpp.smscsim.DeliveryInfoSender;
import org.smpp.smscsim.PDUProcessorGroup;
import org.smpp.smscsim.SMSCListenerImpl;
import org.smpp.smscsim.SMSCSession;
import org.smpp.smscsim.ShortMessageStore;
import org.smpp.smscsim.SimulatorPDUProcessor;
import org.smpp.smscsim.SimulatorPDUProcessorFactory;
import org.smpp.smscsim.util.Table;

/** TODO Descripción de las responsabilidades de la clase, patrones utilizados, algoritmos utilizados.
 *
 * @author Orlando Ramos &lt;orlando.ramos@amk-technologies.com&gt;
 * @version 1.0.0
 * @since 1.0.0
 */
public class SIMSimulator {
    /**
     * Logger for class.
     */
    private static final Logger LOGGER = LogManager.getLogger(SIMSimulator.class.getName());

    /**
     * Listener que ayuda a simular SMS.
     */
    public SMSCListenerImpl             smscListener;
    /**
     * Contenedor de procesos.
     */
    public PDUProcessorGroup            processors;
    /**
     * Contenedor de SMS's que ha enviado el cliente.
     */
    public ShortMessageStore            messageStore;
    /**
     * Información del envío.
     */
    public DeliveryInfoSender           deliveryInfoSender;
    /**
     * Representa una tabla con los registros de los usuarios.
     */
    public Table                        users;
    /**
     * Fábrica de SimulatorPDUProcessor.
     */
    public SimulatorPDUProcessorFactory factory;

    public String usersFileName = "users.txt";

    /**
     * Creates an instance of SIMSimulator.
     * TODO alguna descripción
     * @author Orlando Ramos &lt;orlando.ramos@amk-technologies.com&gt;
     * @version 1.0.0
     * @param
     */
    public SIMSimulator() {
        super();
    }

    public void init(int port) throws IOException {
        Thread.currentThread().setName("SIM-Simulator");
        LOGGER.debug("setUp: SMCS Simluator");
        // Puerto para el Simulador de SMCS.
        smscListener = new SMSCListenerImpl(port, true);
        processors = new PDUProcessorGroup();
        messageStore = new ShortMessageStore();
        deliveryInfoSender = new DeliveryInfoSender();
        deliveryInfoSender.start();
        InputStream input = this.getClass().getClassLoader().getResourceAsStream(usersFileName);
        users = new Table();
        users.getParser().parse(input);
        factory = new SimulatorPDUProcessorFactory(processors, messageStore, deliveryInfoSender, users);
        factory.setDisplayInfo(true);
        smscListener.setPDUProcessorFactory(factory);
        smscListener.start();
        LOGGER.debug("Simulator Started..");
    }

    public void stop() throws IOException {
        if (smscListener != null) {
            LOGGER.debug("Stopping simulator...");
            synchronized (processors) {
                int procCount = processors.count();
                LOGGER.debug("...process " + procCount);
                SimulatorPDUProcessor proc;
                SMSCSession session;
                for (int i = 0; i < procCount; i++) {
                    proc = (SimulatorPDUProcessor) processors.get(i);
                    session = proc.getSession();
                    session.stop();
                }
            }
            smscListener.stop();
            smscListener = null;
            if (deliveryInfoSender != null) {
                deliveryInfoSender.stop();
            }
            LOGGER.debug("SMCS Simulator Stopped.");
        }
    }

}
