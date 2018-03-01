package org.mqttbee.api.mqtt.mqtt5;

import dagger.internal.Preconditions;
import org.mqttbee.annotations.NotNull;
import org.mqttbee.annotations.Nullable;
import org.mqttbee.api.mqtt.mqtt5.advanced.Mqtt5AdvancedClientData;
import org.mqttbee.mqtt.MqttClientDataImpl;
import org.mqttbee.mqtt.MqttClientExecutorConfigImpl;
import org.mqttbee.mqtt.MqttVersion;
import org.mqttbee.mqtt.advanced.MqttAdvancedClientData;
import org.mqttbee.mqtt.datatypes.MqttClientIdentifierImpl;
import org.mqttbee.mqtt5.Mqtt5ClientImpl;
import org.mqttbee.util.MustNotBeImplementedUtil;

/**
 * @author Silvio Giebl
 */
public class Mqtt5ClientBuilder {

    private final MqttClientIdentifierImpl identifier;
    private final String serverHost;
    private final int serverPort;
    private final boolean usesSSL;
    private final MqttClientExecutorConfigImpl executorConfig;

    private boolean followsRedirects = false;
    private boolean allowsServerReAuth = false;
    private MqttAdvancedClientData advancedClientData;

    public Mqtt5ClientBuilder(
            @NotNull final MqttClientIdentifierImpl identifier, @NotNull final String serverHost, final int serverPort,
            final boolean usesSSL, @NotNull final MqttClientExecutorConfigImpl executorConfig) {

        Preconditions.checkNotNull(identifier);
        Preconditions.checkNotNull(serverHost);

        this.identifier = identifier;
        this.serverHost = serverHost;
        this.serverPort = serverPort;
        this.usesSSL = usesSSL;
        this.executorConfig = executorConfig;
    }

    @NotNull
    public Mqtt5ClientBuilder followingRedirects(final boolean followsRedirects) {
        this.followsRedirects = followsRedirects;
        return this;
    }

    @NotNull
    public Mqtt5ClientBuilder allowingServerReAuth(final boolean allowsServerReAuth) {
        this.allowsServerReAuth = allowsServerReAuth;
        return this;
    }

    @NotNull
    public Mqtt5ClientBuilder withAdvanced(@Nullable final Mqtt5AdvancedClientData advancedClientData) {
        this.advancedClientData =
                MustNotBeImplementedUtil.checkNullOrNotImplemented(advancedClientData, MqttAdvancedClientData.class);
        return this;
    }

    @NotNull
    public Mqtt5Client reactive() {
        return new Mqtt5ClientImpl(buildClientData());
    }

    private MqttClientDataImpl buildClientData() {
        return new MqttClientDataImpl(MqttVersion.MQTT_5_0, identifier, serverHost, serverPort, usesSSL,
                followsRedirects, allowsServerReAuth, executorConfig, advancedClientData);
    }

}
