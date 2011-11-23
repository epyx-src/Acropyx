/*******************************************************************************
 * Copyright (c) 2011 epyx SA.
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Affero General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Affero General Public License along
 * with this program.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package ch.acropyx;

import java.beans.PropertyEditorSupport;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CustomDateBinder extends PropertyEditorSupport {

    private final List<String> formats;

    public CustomDateBinder(List<String> formats) {
        List<String> formatList = new ArrayList<String>(formats.size());
        for (Object format : formats) {
            formatList.add(format.toString()); // Force String values (eg. for GStrings)
        }
        this.formats = Collections.unmodifiableList(formatList);
    }

    @Override
    public void setAsText(String s) throws IllegalArgumentException {
        if (s != null) {
            for (String format : formats) {
                // Need to create the SimpleDateFormat every time, since it's not thead-safe
                SimpleDateFormat df = new SimpleDateFormat(format);
                try {
                    setValue(df.parse(s));
                    return;
                } catch (ParseException e) {
                    // Ignore
                }
            }
            throw new IllegalArgumentException("Unable to find a date converter");
        }
    }
}
