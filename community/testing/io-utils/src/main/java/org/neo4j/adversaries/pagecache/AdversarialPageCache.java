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
package org.neo4j.adversaries.pagecache;

import org.eclipse.collections.api.set.ImmutableSet;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.neo4j.adversaries.Adversary;
import org.neo4j.io.pagecache.IOController;
import org.neo4j.io.pagecache.PageCache;
import org.neo4j.io.pagecache.PagedFile;
import org.neo4j.io.pagecache.buffer.IOBufferFactory;

import static java.nio.file.StandardOpenOption.CREATE;

/**
 * A {@linkplain PageCache page cache} that wraps another page cache and an {@linkplain Adversary adversary} to provide
 * a misbehaving page cache implementation for testing.
 * <p>
 * Depending on the adversary each operation can throw either {@link RuntimeException} like {@link SecurityException}
 * or {@link IOException} like {@link NoSuchFileException}.
 */
@SuppressWarnings( "unchecked" )
public class AdversarialPageCache implements PageCache
{
    private final PageCache delegate;
    private final Adversary adversary;

    public AdversarialPageCache( PageCache delegate, Adversary adversary )
    {
        this.delegate = Objects.requireNonNull( delegate );
        this.adversary = Objects.requireNonNull( adversary );
    }

    @Override
    public PagedFile map( Path path, int pageSize, String databaseName, ImmutableSet<OpenOption> openOptions,
            IOController ioController ) throws IOException
    {
        if ( openOptions.contains( CREATE ) )
        {
            adversary.injectFailure( IOException.class, SecurityException.class );
        }
        else
        {
            adversary.injectFailure( NoSuchFileException.class, IOException.class, SecurityException.class );
        }
        PagedFile pagedFile = delegate.map( path, pageSize, databaseName, openOptions, ioController );
        return new AdversarialPagedFile( pagedFile, adversary );
    }

    @Override
    public Optional<PagedFile> getExistingMapping( Path path ) throws IOException
    {
        adversary.injectFailure( IOException.class, SecurityException.class );
        final Optional<PagedFile> optional = delegate.getExistingMapping( path );
        return optional.map( pagedFile -> new AdversarialPagedFile( pagedFile, adversary ) );
    }

    @Override
    public List<PagedFile> listExistingMappings() throws IOException
    {
        adversary.injectFailure( IOException.class, SecurityException.class );
        List<PagedFile> list = delegate.listExistingMappings();
        for ( int i = 0; i < list.size(); i++ )
        {
            list.set( i, new AdversarialPagedFile( list.get( i ), adversary ) );
        }
        return list;
    }

    @Override
    public void flushAndForce() throws IOException
    {
        adversary.injectFailure( NoSuchFileException.class, IOException.class, SecurityException.class );
        delegate.flushAndForce();
    }

    @Override
    public void close()
    {
        adversary.injectFailure( IllegalStateException.class );
        delegate.close();
    }

    @Override
    public int pageSize()
    {
        return delegate.pageSize();
    }

    @Override
    public int pageReservedBytes()
    {
        return delegate.pageReservedBytes();
    }

    @Override
    public long maxCachedPages()
    {
        return delegate.maxCachedPages();
    }

    @Override
    public IOBufferFactory getBufferFactory()
    {
        return delegate.getBufferFactory();
    }
}
