package com.amk.smpp.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.PowerMockRunner;

/** TODO Descripción de las responsabilidades de la clase, patrones utilizados, algoritmos utilizados.
 *
 * @author Orlando Ramos &lt;orlando.ramos@amk-technologies.com&gt;
 * @version 1.0.0
 * @since 1.0.0
 */
@PowerMockIgnore("javax.management.*")
@RunWith(PowerMockRunner.class)
public class BindingTypeTest {
    /**
     * Logger for class.
     */
    private static final Logger LOGGER = LogManager.getLogger(BindingTypeTest.class.getSimpleName());

    /**
     * Configuración del Thread.
     */
    private void setUpThread() {
        Thread.currentThread().setName("BindingTypeTest");
    }

    @Before
    public void setUp() throws Exception {
        setUpThread();
        LOGGER.debug("setUp:");
    }

    @Test
    public void valueOf() throws Exception {
        LOGGER.debug("valueOf: ");
        BindingType expectedBindingType = BindingType.valueOf(BindingType.TRX.toString());
        Assert.assertEquals(expectedBindingType, BindingType.TRX);
        BindingType otherExpectedBindingType = BindingType.valueOf(BindingType.TX.getBindId());
        Assert.assertEquals(otherExpectedBindingType, BindingType.TX);
        try {
            BindingType.valueOf("someBindType");
            Assert.fail();
        } catch (Exception e) {
            // error expected
        }
    }

    @Test
    public void getBindId() throws Exception {
        LOGGER.debug("getBindId: ");
        Assert.assertEquals(1, BindingType.TX.getBindId());
        Assert.assertEquals(2, BindingType.RX.getBindId());
        Assert.assertEquals(3, BindingType.TRX.getBindId());
        try {
            BindingType.valueOf(4);
            Assert.fail();
        } catch (Exception e) {
            // error expected
        }
    }

    @Test
    public void getDescription() throws Exception {
        LOGGER.debug("getDescription: ");
        Assert.assertEquals("An ESME bound as a transmitter may send short messages to a Message Center", BindingType.TX.getDescription());
        Assert.assertEquals("An ESME bound as a receiver may receive short messages from a Message Center.", BindingType.RX.getDescription());
        Assert.assertEquals("An ESME bound as a Transceiver is authorised to use all operations supported by a Transmitter ESME and a Receiver ESME.", BindingType.TRX.getDescription());
        Assert.assertFalse("someBind".equals(BindingType.TRX.getDescription()));
    }

}