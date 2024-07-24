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

package org.apache.shardingsphere.encrypt.algorithm.standard;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.shardingsphere.encrypt.spi.EncryptAlgorithm;
import org.apache.shardingsphere.infra.algorithm.core.context.AlgorithmSQLContext;
import org.apache.shardingsphere.infra.algorithm.core.exception.AlgorithmInitializationException;
import org.apache.shardingsphere.infra.spi.type.typed.TypedSPILoader;
import org.apache.shardingsphere.test.util.PropertiesBuilder;
import org.apache.shardingsphere.test.util.PropertiesBuilder.Property;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.mockito.MockedStatic;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;

class AESEncryptAlgorithmTest {
    
    private EncryptAlgorithm encryptAlgorithm;
    
    @BeforeEach
    void setUp() {
        encryptAlgorithm = TypedSPILoader.getService(EncryptAlgorithm.class, "AES", PropertiesBuilder.build(new Property("aes-key-value", "test"), new Property("digest-algorithm-name", "SHA-1")));
    }
    
    @Test
    void assertDigestAlgorithm() {
        MockedStatic<DigestUtils> digestUtilsMockedStatic = mockStatic(DigestUtils.class, Answers.CALLS_REAL_METHODS);
        TypedSPILoader.getService(EncryptAlgorithm.class, "AES", PropertiesBuilder.build(new Property("aes-key-value", "test"), new Property("digest-algorithm-name", "SHA-1")));
        digestUtilsMockedStatic.verify(() -> DigestUtils.getDigest("SHA-1"), times(1));
        digestUtilsMockedStatic.close();
    }
    
    @Test
    void assertCreateNewInstanceWithoutAESKey() {
        assertThrows(AlgorithmInitializationException.class, () -> TypedSPILoader.getService(EncryptAlgorithm.class, "AES"));
    }
    
    @Test
    void assertCreateNewInstanceWithEmptyAESKey() {
        assertThrows(AlgorithmInitializationException.class, () -> encryptAlgorithm.init(PropertiesBuilder.build(new Property("aes-key-value", ""))));
    }
    
    @Test
    void assertEncrypt() {
        Object actual = encryptAlgorithm.encrypt("test", mock(AlgorithmSQLContext.class));
        assertThat(actual, is("dSpPiyENQGDUXMKFMJPGWA=="));
    }
    
    @Test
    void assertEncryptNullValue() {
        assertNull(encryptAlgorithm.encrypt(null, mock(AlgorithmSQLContext.class)));
    }
    
    @Test
    void assertDecrypt() {
        Object actual = encryptAlgorithm.decrypt("dSpPiyENQGDUXMKFMJPGWA==", mock(AlgorithmSQLContext.class));
        assertThat(actual.toString(), is("test"));
    }
    
    @Test
    void assertDecryptNullValue() {
        assertNull(encryptAlgorithm.decrypt(null, mock(AlgorithmSQLContext.class)));
    }
}
