package com.amk.smpp.core;

import java.util.Objects;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.smpp.Connection;
import org.smpp.TCPIPConnection;
import org.smpp.pdu.Address;

import com.amk.smpp.operation.PDUOperation;
import com.amk.smpp.operation.PDUOperationProperties;
import com.amk.smpp.operation.PDUOperationPropertiesBuilder;
import com.amk.smpp.operation.PDUOperationTypes;
import com.amk.smpp.sim.SIMSimulator;
import com.amk.smpp.util.Message;

/** TODO Descripción de las responsabilidades de la clase, patrones utilizados, algoritmos utilizados.
 *
 * @author Orlando Ramos &lt;orlando.ramos@amk-technologies.com&gt;
 * @version 1.0.0
 * @since 1.0.0
 */
public class BindingManagerTest {
    private SIMSimulator simSimulator;

    /**
     * SMPP Connection
     */
    private Connection             connection;
    private BindingManager         bindingManager;
    private PDUOperationProperties props;
    private PDUOperation           pduOperation;

    private void setUpSimulator() throws Exception {
        if (Objects.isNull(simSimulator)) {
            simSimulator = new SIMSimulator();
            simSimulator.stop();
            simSimulator.init(2302);
        }

    }

    /**
     * Configuración del Thread.
     */
    private void setUpThread() {
        Thread.currentThread().setName("BindingManagerTest");
    }

    @Before
    public void setUp() throws Exception {
        setUpThread();
        props = new PDUOperationPropertiesBuilder()
                .setSourceAddress(new Address("5529094190"))
                .setDestAddress(new Address[]{new Address("5529094190")})
                .build();
        Message smsMessage = new Message();
        smsMessage.setBody("Mi mensaje de prueba");
        pduOperation = PDUOperation
                .newBuilder()
                .withOperationProps(props)
                .withOperationType(PDUOperationTypes.SUBMIT_SMS)
                .withAsynchronous(false)
                .withSmsMessage(smsMessage)
                .withBindingType(BindingType.TRX)
                .build();
    }

    @Test
    public void bindManager() throws Exception {
        connection = new TCPIPConnection("0.0.0.0", 2302);
        bindingManager = new BindingManager("hugo", "ggoohu", connection);
        try {
            connection.close();
            bindingManager = new BindingManager("hugo", "ggoohu", null);
        } catch (final Exception e) {
            // error expected
        }
        connection = null;
    }

    @Test
    public void bind() throws Exception {
        setUpSimulator();
        connection = new TCPIPConnection("0.0.0.0", 2302);
        bindingManager = new BindingManager(null, null, connection);
        bindingManager.setSystemId("hugo");
        bindingManager.setPassword("ggoohu");
        Assert.assertFalse(bindingManager.isBound());
        bindingManager.unBind();
        Assert.assertNotNull(bindingManager.bind(pduOperation));
        Assert.assertTrue(bindingManager.isBound());
        bindingManager.unBind();
        Assert.assertNotNull(bindingManager.getSession());
        Assert.assertNotNull(bindingManager.getPassword());
        Assert.assertNotNull(bindingManager.getSystemId());
        connection.close();
    }

}