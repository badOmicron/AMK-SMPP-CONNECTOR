package com.amk.smpp.operation;

import java.util.Date;

import org.junit.Assert;
import org.junit.Test;
import org.smpp.Data;
import org.smpp.pdu.Address;

/** TODO Descripción de las responsabilidades de la clase, patrones utilizados, algoritmos utilizados.
 *
 * @author Orlando Ramos &lt;orlando.ramos@amk-technologies.com&gt;
 * @version 1.0.0
 * @since 1.0.0
 */
public class PDUOperationPropertiesBuilderTest {
    @Test
    public void build() throws Exception {
        // Reciviendo mensaje.
        PDUOperationProperties props = new PDUOperationPropertiesBuilder()
                .setSourceAddress(new Address("5529094190"))
                .setDestAddress(new Address[]{new Address("5529094190")})
                .setScheduleDeliveryTime(new Date())
                .setSystemType(Data.DFLT_SYSTYPE)
                .setServiceType(Data.DFLT_SRVTYPE)
                .setValidityPeriod("YYmmssStz")
                .setNumberOfDestination(2)
                .setEsmClass(Data.DFLT_ESM_CLASS)
                .setProtocolId(Data.DFLT_PROTOCOLID)
                .setRegisteredDelivery(Data.DFLT_REG_DELIVERY)
                .setReplaceIfPresentFlag(Data.DFTL_REPLACE_IFP)
                .setDataCoding(Data.DFLT_DATA_CODING)
                .setSmDefaultMsgId(Data.DFLT_DFLTMSGID)
                .build();
        Assert.assertNotNull(props);

        PDUOperationProperties otherProps = new PDUOperationPropertiesBuilder()
                .setSourceAddress(new Address("5529094190"))
                .setDestAddress(null)
                .setScheduleDeliveryTime(null)
                .setSystemType(Data.DFLT_SYSTYPE)
                .setServiceType(Data.DFLT_SRVTYPE)
                .setValidityPeriod("YYmmssStz")
                .setNumberOfDestination(2)
                .setEsmClass(Data.DFLT_ESM_CLASS)
                .setProtocolId(Data.DFLT_PROTOCOLID)
                .setRegisteredDelivery(Data.DFLT_REG_DELIVERY)
                .setReplaceIfPresentFlag(Data.DFTL_REPLACE_IFP)
                .setDataCoding(Data.DFLT_DATA_CODING)
                .setSmDefaultMsgId(Data.DFLT_DFLTMSGID)
                .build();

    }

}