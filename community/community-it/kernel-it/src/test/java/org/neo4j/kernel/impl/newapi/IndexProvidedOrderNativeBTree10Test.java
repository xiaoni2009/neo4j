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
package org.neo4j.kernel.impl.newapi;

import org.neo4j.configuration.GraphDatabaseSettings;
import org.neo4j.graphdb.schema.IndexType;

abstract class IndexProvidedOrderNativeBTree10Test extends AbstractIndexProvidedOrderTest
{
    @Override
    GraphDatabaseSettings.SchemaIndex getSchemaIndex()
    {
        return GraphDatabaseSettings.SchemaIndex.NATIVE_BTREE10;
    }

    @Override
    IndexType getIndexType()
    {
        return IndexType.BTREE;
    }
}
