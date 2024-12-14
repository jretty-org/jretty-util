/*
 * Copyright (C) 2013-2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 */
package org.jretty.util;

import java.io.Serializable;
import java.util.function.Function;

/**
 * 支持序列化的 Function
 * （使用 [M extends Function<T, R>, Serializable]类型参数，JVM将自动将xx::getXX转换成带writeReplace方法的类）
 * {@link org.jretty.util.LambdaUtils#extract}
 */
@FunctionalInterface
public interface Fn<T, R> extends Function<T, R>, Serializable {
}