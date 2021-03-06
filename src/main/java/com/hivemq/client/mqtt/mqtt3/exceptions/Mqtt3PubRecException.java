/*
 * Copyright 2018 dc-square and the HiveMQ MQTT Client Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.hivemq.client.mqtt.mqtt3.exceptions;

import com.hivemq.client.internal.mqtt.message.publish.pubrec.mqtt3.Mqtt3PubRecView;
import com.hivemq.client.mqtt.mqtt3.message.publish.pubrec.Mqtt3PubRec;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Silvio Giebl
 * @since 1.0
 */
public class Mqtt3PubRecException extends Mqtt3MessageException {

    public Mqtt3PubRecException(final @Nullable String message, final @Nullable Throwable cause) {
        super(message, cause);
    }

    private Mqtt3PubRecException(final @NotNull Mqtt3PubRecException e) {
        super(e);
    }

    @Override
    protected @NotNull Mqtt3PubRecException copy() {
        return new Mqtt3PubRecException(this);
    }

    @Override
    public @NotNull Mqtt3PubRec getMqttMessage() {
        return Mqtt3PubRecView.INSTANCE;
    }
}
