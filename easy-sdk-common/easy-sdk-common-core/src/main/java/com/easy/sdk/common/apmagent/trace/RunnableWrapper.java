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
 */

package com.easy.sdk.common.apmagent.trace;




/**
 * 实现了Runnable接口，若想要做异步线程链路监控必须使用此类的构造方法，或of方法创建
 * @param <V>
 */
public class RunnableWrapper implements Runnable {

    final Runnable runnable;

    private String traceId;

    public String getSegmentId() {
        return segmentId;
    }

    public void setSegmentId(String segmentId) {
        this.segmentId = segmentId;
    }

    private String segmentId;

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }



    public RunnableWrapper(Runnable runnable) {
        this.runnable = runnable;
    }

    public static RunnableWrapper of(Runnable runnable) {
        return new RunnableWrapper(runnable);
    }

    @Override
    public void run() {
        this.runnable.run();
    }


}