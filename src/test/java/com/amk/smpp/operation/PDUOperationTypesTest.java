package com.amk.smpp.operation;

import org.junit.Assert;
import org.junit.Test;

/** TODO Descripción de las responsabilidades de la clase, patrones utilizados, algoritmos utilizados.
 *
 * @author Orlando Ramos &lt;orlando.ramos@amk-technologies.com&gt;
 * @version 1.0.0
 * @since 1.0.0
 */
public class PDUOperationTypesTest {
    @Test
    public void valueOf() throws Exception {
        PDUOperationTypes cancel = PDUOperationTypes.valueOf(PDUOperationTypes.CANCEL.name());
        Assert.assertEquals(cancel, PDUOperationTypes.CANCEL);

        PDUOperationTypes submit = PDUOperationTypes.valueOf(PDUOperationTypes.SUBMIT_SMS.getOperationId());
        Assert.assertEquals(submit, PDUOperationTypes.SUBMIT_SMS);
        Assert.assertEquals("send SMS message to a device", submit.getDescription());

        try {
            PDUOperationTypes.valueOf(9);
        } catch (final Exception e) {
            // error expected.
        }
    }

}