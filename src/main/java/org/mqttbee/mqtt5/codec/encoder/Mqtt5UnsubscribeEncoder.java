package org.mqttbee.mqtt5.codec.encoder;

import com.google.common.collect.ImmutableList;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import org.mqttbee.annotations.NotNull;
import org.mqttbee.mqtt5.codec.Mqtt5DataTypes;
import org.mqttbee.mqtt5.message.Mqtt5MessageType;
import org.mqttbee.mqtt5.message.Mqtt5TopicFilterImpl;
import org.mqttbee.mqtt5.message.unsubscribe.Mqtt5UnsubscribeImpl;
import org.mqttbee.mqtt5.message.unsubscribe.Mqtt5UnsubscribeInternal;

import javax.inject.Singleton;

/**
 * @author Silvio Giebl
 */
@Singleton
public class Mqtt5UnsubscribeEncoder implements Mqtt5MessageEncoder<Mqtt5UnsubscribeInternal> {

    public static final Mqtt5UnsubscribeEncoder INSTANCE = new Mqtt5UnsubscribeEncoder();

    private static final int FIXED_HEADER = (Mqtt5MessageType.UNSUBSCRIBE.getCode() << 4) | 0b0010;
    private static final int VARIABLE_HEADER_FIXED_LENGTH = 2; // packet identifier

    @Override
    public void encode(
            @NotNull final Mqtt5UnsubscribeInternal unsubscribeInternal, @NotNull final Channel channel,
            @NotNull final ByteBuf out) {

        encodeFixedHeader(unsubscribeInternal, out);
        encodeVariableHeader(unsubscribeInternal, out);
        encodePayload(unsubscribeInternal, out);
    }

    public int encodedRemainingLength(@NotNull final Mqtt5UnsubscribeImpl unsubscribe) {
        int remainingLength = VARIABLE_HEADER_FIXED_LENGTH;

        final ImmutableList<Mqtt5TopicFilterImpl> topicFilters = unsubscribe.getTopicFilters();
        for (int i = 0; i < topicFilters.size(); i++) {
            remainingLength += topicFilters.get(i).encodedLength();
        }

        return remainingLength;
    }

    public int encodedPropertyLength(@NotNull final Mqtt5UnsubscribeImpl unsubscribe) {
        return unsubscribe.getUserProperties().encodedLength();
    }

    private void encodeFixedHeader(
            @NotNull final Mqtt5UnsubscribeInternal unsubscribeInternal, @NotNull final ByteBuf out) {

        out.writeByte(FIXED_HEADER);
        Mqtt5DataTypes.encodeVariableByteInteger(
                unsubscribeInternal.encodedRemainingLength(Mqtt5DataTypes.MAXIMUM_PACKET_SIZE_LIMIT), out); // TODO
    }

    private void encodeVariableHeader(
            @NotNull final Mqtt5UnsubscribeInternal unsubscribeInternal, @NotNull final ByteBuf out) {

        out.writeShort(unsubscribeInternal.getPacketIdentifier());
        encodeProperties(unsubscribeInternal, out);
    }

    private void encodeProperties(
            @NotNull final Mqtt5UnsubscribeInternal unsubscribeInternal, @NotNull final ByteBuf out) {

        Mqtt5DataTypes.encodeVariableByteInteger(
                unsubscribeInternal.encodedPropertyLength(Mqtt5DataTypes.MAXIMUM_PACKET_SIZE_LIMIT), out); // TODO
        unsubscribeInternal.getWrapped().encodeUserProperties(Mqtt5DataTypes.MAXIMUM_PACKET_SIZE_LIMIT, out); // TODO
    }

    private void encodePayload(
            @NotNull final Mqtt5UnsubscribeInternal unsubscribeInternal, @NotNull final ByteBuf out) {

        final ImmutableList<Mqtt5TopicFilterImpl> topicFilters = unsubscribeInternal.getWrapped().getTopicFilters();
        for (int i = 0; i < topicFilters.size(); i++) {
            topicFilters.get(i).to(out);
        }
    }

}
