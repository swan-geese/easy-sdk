/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
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

package com.easy.sdk.common.apmagent.trace;

import java.util.function.Supplier;


public class SupplierWrapper<V> implements Supplier<V> {

    private String traceId;

    private String segmentId;

    final Supplier<V> supplier;

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public String getSegmentId() {
        return segmentId;
    }

    public void setSegmentId(String segmentId) {
        this.segmentId = segmentId;
    }


    public static <V> SupplierWrapper<V> of(Supplier<V> r) {
        return new SupplierWrapper<>(r);
    }

    public SupplierWrapper(Supplier<V> supplier) {
        this.supplier = supplier;
    }

    @Override
    public V get() {
        return supplier.get();
    }
}
