/*
 *      File: SmppUtil.java
 *    Author: Orlando Ramos <orlando.ramos@amk-technologies.com>
 *      Date: Jul 21, 2017
 * Copyright: AMK Technologies, S.A. de C.V. 2017
 */

package com.amk.smpp.util;


import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.amk.smpp.rules.PDUOperationsValidator;


/**
 * Support class for SMPP operations.
 * @author Orlando Ramos &lt;orlando.ramos@amk-technologies.com&gt;
 * @version 1.0.0
 * @since 1.0.0
 */
public class SmppUtil {
    /**
     * Logger for class.
     */
    private             Logger LOGGER                         = LogManager.getLogger(PDUOperationsValidator.class.getName());
    /**
     * Scheduled Delivery Time Format.
     */
    public static final String SCHEDULED_DELIVERY_TIME_FROMAT = "YYMMDDhhmmssS";
//    tnnp
    /**
     * Invoke method error msg.
     */
    public static final String ERROR_INVOKE                   = "ERROR";
    public static final String NONE_VALUE                     = "NONE";


    /**
     * Creates an instance of SmppUtil.
     * @author Orlando Ramos &lt;orlando.ramos@amk-technologies.com&gt;
     */
    private SmppUtil() {
        super();
    }

    /**
     *  Applies a date format 'YYMMDDhhmmssSSSS' and transforms them to letter.
     * @param scheduledDeliveryTime Date of delivery of the SMS.
     * @return Date of delivery of the message with format.
     */
    public static String transformDate(final Date scheduledDeliveryTime) {
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(SCHEDULED_DELIVERY_TIME_FROMAT);
        final Date today = new Date();
        if (scheduledDeliveryTime.before(today)) {
            return simpleDateFormat.format(today);
        }
        return simpleDateFormat.format(scheduledDeliveryTime);
    }

    /**
     *  An object is parsed, it is applied introspection to be able to obtain all its properties and their values i'll
     *  be stored in a {@link Map}.
     * @param bean to be parsed.
     * @return a map with all the property-value beans.<br/>
     * <code>{@literal Map<PropertyName,PropertyValue>}</code>
     */
    public static Map< String, Object > beanProperties(final Object bean) {
        if (Objects.isNull(bean)) {
            throw new IllegalArgumentException("[X] error, null bean ");
        }
        try {
            final Map< String, Object > map = new HashMap<>();
            // Se obtiene la informacion del la clase por medio de refleccion.
            // en este case se busca la descripción de todas las propiedades del objeto.
            Arrays.stream(Introspector.getBeanInfo(bean.getClass(), Object.class).getPropertyDescriptors())
                    // se filtran esas propiedades buscando los GETTERS
                    .filter(propertyDescriptor -> Objects.nonNull(propertyDescriptor.getReadMethod()))
                    // se obtiene el nombre de la propiedad y el valor de la propiedad haciendo la invocación del método
                    // los resultados se almacenan en un mapa <nombre de la propiedad, valor>
                    .forEach(propertyDescriptor -> map.put(propertyDescriptor.getName(),
                            invoke(propertyDescriptor, bean)));
            return map;
        } catch (final IntrospectionException e) {
            // si algo falla se retorna un mapa vacio

            return Collections.emptyMap();
        }
    }

    /**
     * Support method for {@link #beanProperties(Object)} <br/>
     * Helps to invoke the getter of the parsed bean.<br/>
     * If something bad in the invoked method the returned value will be {@link #ERROR_INVOKE}
     * @param pd PropertyDescriptor to be invoked.
     * @param bean bean that contains the invoked getter.
     * @return Value obtained from method invoked.
     * */
    private static Object invoke(final PropertyDescriptor pd, final Object bean) {
        try {
            return (Objects.isNull(pd.getReadMethod().invoke(bean))
                    || pd.getReadMethod().invoke(bean).toString().isEmpty()) ? NONE_VALUE : pd.getReadMethod().invoke(bean);
        } catch (InvocationTargetException | IllegalAccessException e) {
            return ERROR_INVOKE;
        }
    }
}
