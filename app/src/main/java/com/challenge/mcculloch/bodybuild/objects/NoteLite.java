package com.challenge.mcculloch.bodybuild.objects;

import com.challenge.mcculloch.bodybuild.Constants;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by wooan on 10/4/2015.
 */
@DatabaseTable(tableName = Constants.memberNotes_table)
public class NoteLite {
    @DatabaseField
    private String UserNote;
    @DatabaseField
    private int MemberId;

    public NoteLite() {
    }

    public NoteLite(String userNote, int memberId) {
        UserNote = userNote;
        MemberId = memberId;
    }

    public String getUserNote() {
        return UserNote;
    }

    public void setUserNote(String userNote) {
        UserNote = userNote;
    }

    public int getMemberId() {
        return MemberId;
    }

    public void setMemberId(int memberId) {
        MemberId = memberId;
    }
}
