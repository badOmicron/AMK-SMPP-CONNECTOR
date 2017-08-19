package com.amk.smpp.util;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

/** TODO Descripción de las responsabilidades de la clase, patrones utilizados, algoritmos utilizados.
 *
 * @author Orlando Ramos &lt;orlando.ramos@amk-technologies.com&gt;
 * @version 1.0.0
 * @since 1.0.0
 */
@RunWith(PowerMockRunner.class)
public class MessageTest {

    @Test
    public void Message() throws Exception {
        String messageId = "id#123";
        String messageBody = "Body";
        Message message = new Message(messageId, messageBody);
        Assert.assertNotNull(message);
        Assert.assertEquals(messageId, message.getId());
        Assert.assertEquals(messageBody, message.getBody());
    }

}