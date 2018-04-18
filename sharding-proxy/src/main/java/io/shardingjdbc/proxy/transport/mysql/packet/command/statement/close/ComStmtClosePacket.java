/*
 * Copyright 1999-2015 dangdang.com.
 * <p>
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
 * </p>
 */

package io.shardingjdbc.proxy.transport.mysql.packet.command.statement.close;

import io.shardingjdbc.proxy.transport.mysql.packet.MySQLPacketPayload;
import io.shardingjdbc.proxy.transport.mysql.packet.command.CommandPacket;
import io.shardingjdbc.proxy.transport.mysql.packet.command.CommandResponsePackets;
import lombok.extern.slf4j.Slf4j;

/**
 * COM_STMT_CLOSE command packet.
 * @see <a href="https://dev.mysql.com/doc/internals/en/com-stmt-close.html">COM_QUERY</a>
 *
 * @author zhangyonglun
 */
@Slf4j
public class ComStmtClosePacket extends CommandPacket {
    
    private final int statementId;
    
    public ComStmtClosePacket(final int sequenceId, final MySQLPacketPayload mysqlPacketPayload) {
        super(sequenceId);
        statementId = mysqlPacketPayload.readInt4();
    }
    
    @Override
    public void write(final MySQLPacketPayload mysqlPacketPayload) {
    }
    
    @Override
    public CommandResponsePackets execute() {
        log.debug("COM_STMT_CLOSE received for Sharding-Proxy: {}", statementId);
        return new CommandResponsePackets(new DummyPacket());
    }
}
