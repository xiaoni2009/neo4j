/*
 * Copyright (c) "Neo4j"
 * Neo4j Sweden AB [http://neo4j.com]
 *
 * This file is part of Neo4j.
 *
 * Neo4j is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.neo4j.kernel.api;

import org.neo4j.internal.kernel.api.CursorFactory;
import org.neo4j.internal.kernel.api.IndexMonitor;
import org.neo4j.internal.kernel.api.QueryContext;
import org.neo4j.internal.kernel.api.Read;
import org.neo4j.io.pagecache.context.CursorContext;
import org.neo4j.memory.MemoryTracker;
import org.neo4j.storageengine.api.txstate.ReadableTransactionState;

public class WorkerQueryContext implements QueryContext
{
    private final CursorContext cursorContext;
    private final QueryContext delegate;

    public WorkerQueryContext( QueryContext delegate, CursorContext cursorContext )
    {
        this.delegate = delegate;
        this.cursorContext = cursorContext;
    }

    @Override
    public Read getRead()
    {
        return delegate.getRead();
    }

    @Override
    public CursorFactory cursors()
    {
        return delegate.cursors();
    }

    @Override
    public ReadableTransactionState getTransactionStateOrNull()
    {
        return delegate.getTransactionStateOrNull();
    }

    @Override
    public CursorContext cursorContext()
    {
        return cursorContext;
    }

    @Override
    public MemoryTracker memoryTracker()
    {
        return delegate.memoryTracker();
    }

    @Override
    public IndexMonitor monitor()
    {
        return delegate.monitor();
    }
}
