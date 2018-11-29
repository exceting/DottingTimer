/**
 * sharemer.com Inc.
 * Copyright (c) 2009-2018 All Rights Reserved.
 */
package dotting.timer.ui.po;

import com.google.common.collect.Lists;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @author sunqinwen
 * @version \: Span.java,v 0.1 2018-11-19 13:48
 */
public class Span {

    private long id;

    private long trace_id;

    private long span_id;

    private long parent_id;

    private long start;

    private long end;

    private int is_async;

    private int is_error;

    private int expect;

    private String short_moudle;

    private String moudle;

    private String short_title;

    private String title;

    private String tags;

    private String ctime;

    private int duration;

    private int merge;

    private int avg_duration;

    private int min_duration;

    private int max_duration;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTrace_id() {
        return trace_id;
    }

    public void setTrace_id(long trace_id) {
        this.trace_id = trace_id;
    }

    public long getSpan_id() {
        return span_id;
    }

    public void setSpan_id(long span_id) {
        this.span_id = span_id;
    }

    public long getParent_id() {
        return parent_id;
    }

    public void setParent_id(long parent_id) {
        this.parent_id = parent_id;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    public int getIs_async() {
        return is_async;
    }

    public void setIs_async(int is_async) {
        this.is_async = is_async;
    }

    public int getIs_error() {
        return is_error;
    }

    public void setIs_error(int is_error) {
        this.is_error = is_error;
    }

    public int getExpect() {
        return expect;
    }

    public void setExpect(int expect) {
        this.expect = expect;
    }

    public String getMoudle() {
        return moudle;
    }

    public void setMoudle(String moudle) {
        this.moudle = moudle;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getCtime() {
        return ctime;
    }

    public void setCtime(String ctime) {
        this.ctime = ctime;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getShort_moudle() {
        return short_moudle;
    }

    public void setShort_moudle(String short_moudle) {
        this.short_moudle = short_moudle;
    }

    public String getShort_title() {
        return short_title;
    }

    public void setShort_title(String short_title) {
        this.short_title = short_title;
    }

    public int getMerge() {
        return merge;
    }

    public void setMerge(int merge) {
        this.merge = merge;
    }

    public int getAvg_duration() {
        return avg_duration;
    }

    public void setAvg_duration(int avg_duration) {
        this.avg_duration = avg_duration;
    }

    public int getMin_duration() {
        return min_duration;
    }

    public void setMin_duration(int min_duration) {
        this.min_duration = min_duration;
    }

    public int getMax_duration() {
        return max_duration;
    }

    public void setMax_duration(int max_duration) {
        this.max_duration = max_duration;
    }

    public static Span getSpanObjByRs(ResultSet rs) throws SQLException {
        Span span = new Span();
        transToSpan(span, rs);
        return span;
    }

    public static List<Span> getSpanObjsByRs(ResultSet rs) throws SQLException {
        List<Span> currentSpan = Lists.newArrayList();
        while (rs.next()) {
            Span span = new Span();
            transToSpan(span, rs);
            currentSpan.add(span);
        }
        return currentSpan;
    }

    public static void transToSpan(Span span, ResultSet rs) throws SQLException {
        span.setId(rs.getLong("id"));
        span.setTrace_id(rs.getLong("trace_id"));
        span.setSpan_id(rs.getLong("span_id"));
        span.setParent_id(rs.getLong("parent_id"));
        span.setStart(rs.getLong("start"));
        span.setEnd(rs.getLong("end"));
        span.setDuration(Math.round((span.getEnd() - span.getStart())));
        span.setIs_async(rs.getInt("is_async"));
        span.setIs_error(rs.getInt("is_error"));
        span.setExpect(rs.getInt("expect"));
        String moudle = rs.getString("moudle");
        span.setMoudle(moudle);
        span.setShort_moudle(moudle.substring(moudle.lastIndexOf('.') + 1, moudle.length()));
        String title = rs.getString("title");
        span.setTitle(title);
        span.setShort_title(title.substring(title.lastIndexOf('.') + 1, title.length()));
        span.setTags(rs.getString("tags"));
        span.setCtime(rs.getString("ctime"));
        span.setMerge(rs.getInt("merge"));
        span.setAvg_duration(rs.getInt("avg_duration"));
        span.setMin_duration(rs.getInt("min_duration"));
        span.setMax_duration(rs.getInt("max_duration"));
    }
}
