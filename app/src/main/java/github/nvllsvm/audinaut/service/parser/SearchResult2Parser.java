/*
 This file is part of Subsonic.

 Subsonic is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 Subsonic is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with Subsonic.  If not, see <http://www.gnu.org/licenses/>.

 Copyright 2009 (C) Sindre Mehus
 */
package github.nvllsvm.audinaut.service.parser;

import android.content.Context;
import github.nvllsvm.audinaut.R;
import github.nvllsvm.audinaut.domain.MusicDirectory;
import github.nvllsvm.audinaut.domain.SearchResult;
import github.nvllsvm.audinaut.domain.Artist;
import github.nvllsvm.audinaut.util.ProgressListener;
import org.xmlpull.v1.XmlPullParser;

import java.io.Reader;
import java.util.List;
import java.util.ArrayList;

/**
 * @author Sindre Mehus
 */
public class SearchResult2Parser extends MusicDirectoryEntryParser {

    public SearchResult2Parser(Context context, int instance) {
		super(context, instance);
	}

    public SearchResult parse(Reader reader, ProgressListener progressListener) throws Exception {
        init(reader);

        List<Artist> artists = new ArrayList<Artist>();
        List<MusicDirectory.Entry> albums = new ArrayList<MusicDirectory.Entry>();
        List<MusicDirectory.Entry> songs = new ArrayList<MusicDirectory.Entry>();
        int eventType;
        do {
            eventType = nextParseEvent();
            if (eventType == XmlPullParser.START_TAG) {
                String name = getElementName();
                if ("artist".equals(name)) {
                    Artist artist = new Artist();
                    artist.setId(get("id"));
                    artist.setName(get("name"));
                    artists.add(artist);
                } else if ("album".equals(name)) {
					MusicDirectory.Entry entry = parseEntry("");
					entry.setDirectory(true);
                    albums.add(entry);
                } else if ("song".equals(name)) {
                    songs.add(parseEntry(""));
                } else if ("error".equals(name)) {
                    handleError();
                }
            }
        } while (eventType != XmlPullParser.END_DOCUMENT);

        validate();

        return new SearchResult(artists, albums, songs);
    }

}