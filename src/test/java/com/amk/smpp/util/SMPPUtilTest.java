package com.amk.smpp.util;

import java.util.Date;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.PowerMockRunner;
import org.smpp.pdu.BindReceiver;
import org.smpp.pdu.BindRequest;
import org.smpp.pdu.BindTransciever;
import org.smpp.pdu.BindTransmitter;

import com.amk.smpp.core.BindingType;
import com.amk.smpp.operation.PDUOperation;
import com.amk.smpp.operation.PDUOperationPropertiesBuilder;

/** TODO Descripción de las responsabilidades de la clase, patrones utilizados, algoritmos utilizados.
 *
 * @author Orlando Ramos &lt;orlando.ramos@amk-technologies.com&gt;
 * @version 1.0.0
 * @since 1.0.0
 */
@PowerMockIgnore("javax.management.*")
@RunWith(PowerMockRunner.class)
public class SMPPUtilTest {
    /**
     * Logger for class.
     */
    private static final Logger LOGGER = LogManager.getLogger(SMPPUtilTest.class);

    @Before
    public void setUp() throws Exception {
        setUpThread();
    }

    /**
     * Configuración del Thread.
     */
    private void setUpThread() {
        Thread.currentThread().setName("SMPPUtilTest");
        LOGGER.debug("init");
    }


    @Test
    public void transformDate() throws Exception {
        // TODO
        String stringDate = SMPPUtil.transformDate(new Date());
        Assert.assertNotNull(stringDate);
    }

    @Test
    public void beanProperties() throws Exception {
        try {
            SMPPUtil.beanProperties(null);
            Assert.fail();
        } catch (final Exception e) {
            // error expected
        }
        Map testMap = SMPPUtil.beanProperties(new PDUOperationPropertiesBuilder().build());
        Assert.assertNotNull(testMap);
    }

    @Test
    public void defineBindingType() throws Exception {
        PDUOperation operation = PDUOperation.newBuilder().withBindingType(BindingType.RX).build();
        BindRequest bindRequest = SMPPUtil.defineBindingType(operation);
        Assert.assertEquals(bindRequest.getClass(), BindReceiver.class);

        operation.setBindingType(BindingType.TX);
        bindRequest = SMPPUtil.defineBindingType(operation);
        Assert.assertEquals(bindRequest.getClass(), BindTransmitter.class);

        operation.setBindingType(BindingType.TRX);
        bindRequest = SMPPUtil.defineBindingType(operation);
        Assert.assertEquals(bindRequest.getClass(), BindTransciever.class);

        operation.setBindingType(null);
        bindRequest = SMPPUtil.defineBindingType(operation);
        Assert.assertEquals(bindRequest.getClass(), BindTransciever.class);

        try {
            operation.setBindingType(BindingType.valueOf(4));
            Assert.fail();
        } catch (final Exception e) {
            //
        }

    }

}