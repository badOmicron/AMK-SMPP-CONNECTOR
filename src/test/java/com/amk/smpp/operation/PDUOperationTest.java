package com.amk.smpp.operation;

import org.junit.Assert;
import org.junit.Test;

import com.amk.smpp.core.BindingType;

/** TODO Descripción de las responsabilidades de la clase, patrones utilizados, algoritmos utilizados.
 *
 * @author Orlando Ramos &lt;orlando.ramos@amk-technologies.com&gt;
 * @version 1.0.0
 * @since 1.0.0
 */
public class PDUOperationTest {
    @Test
    public void newBuilder() throws Exception {
        PDUOperation operation = PDUOperation.newBuilder()
                .withAsynchronous(true)
                .withBindingType(BindingType.RX)
                .withOperationType(PDUOperationTypes.DATA)
                .build();
        Assert.assertNotNull(operation);
    }

}