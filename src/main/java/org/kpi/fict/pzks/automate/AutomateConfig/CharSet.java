package org.kpi.fict.pzks.automate.AutomateConfig;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("charset")
public class CharSet {
    @XStreamAsAttribute
    String name;

    @XStreamAlias("chars")
    @XStreamAsAttribute
    String chars;

    public String getCharsTypeName() {
        return name;
    }

    public String getChars() {
        return chars;
    }
}
