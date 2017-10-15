package com.shreyasbhandare.ruevents.Model.POJO;

public class EventsPerPage
{
    private Data[] data;

    private Paging paging;

    public Data[] getData ()
    {
        return data;
    }

    public void setData (Data[] data)
    {
        this.data = data;
    }

    public Paging getPaging ()
    {
        return paging;
    }

    public void setPaging (Paging paging)
    {
        this.paging = paging;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [data = "+data+", paging = "+paging+"]";
    }

    public class Paging
    {
        private Cursors cursors;

        public Cursors getCursors ()
        {
            return cursors;
        }

        public void setCursors (Cursors cursors)
        {
            this.cursors = cursors;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [cursors = "+cursors+"]";
        }
    }

    public class Cursors
    {
        private String after;

        private String before;

        public String getAfter ()
        {
            return after;
        }

        public void setAfter (String after)
        {
            this.after = after;
        }

        public String getBefore ()
        {
            return before;
        }

        public void setBefore (String before)
        {
            this.before = before;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [after = "+after+", before = "+before+"]";
        }
    }

    public class Data
    {
        private String id;

        private Cover cover;

        private String name;

        private String start_time;

        public String getId ()
        {
            return id;
        }

        public void setId (String id)
        {
            this.id = id;
        }

        public Cover getCover ()
        {
            return cover;
        }

        public void setCover (Cover cover)
        {
            this.cover = cover;
        }

        public String getName ()
        {
            return name;
        }

        public void setName (String name)
        {
            this.name = name;
        }

        public String getStart_time ()
        {
            return start_time;
        }

        public void setStart_time (String start_time)
        {
            this.start_time = start_time;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [id = "+id+", cover = "+cover+", name = "+name+", start_time = "+start_time+"]";
        }
    }

    public class Cover
    {
        private String id;

        private String source;

        private String offset_y;

        private String offset_x;

        public String getId ()
        {
            return id;
        }

        public void setId (String id)
        {
            this.id = id;
        }

        public String getSource ()
        {
            return source;
        }

        public void setSource (String source)
        {
            this.source = source;
        }

        public String getOffset_y ()
        {
            return offset_y;
        }

        public void setOffset_y (String offset_y)
        {
            this.offset_y = offset_y;
        }

        public String getOffset_x ()
        {
            return offset_x;
        }

        public void setOffset_x (String offset_x)
        {
            this.offset_x = offset_x;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [id = "+id+", source = "+source+", offset_y = "+offset_y+", offset_x = "+offset_x+"]";
        }
    }

}
