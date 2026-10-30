package com.gathin.tvhome;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ProfilePillTest {
    @Rule public TemporaryFolder folder = new TemporaryFolder();

    @Test public void noPhotoNoAvatar() {
        assertNull(Config.avatar(null));
        assertNull(Config.avatar(folder.getRoot()));
    }

    @Test public void pngComesBeforeJpg() throws Exception {
        File jpg = folder.newFile("avatar.jpg");
        assertEquals(jpg, Config.avatar(folder.getRoot()));
        File png = folder.newFile("avatar.png");
        assertEquals(png, Config.avatar(folder.getRoot()));
    }

    @Test public void aFolderNamedAvatarIsNotAPhoto() throws Exception {
        folder.newFolder("avatar.png");
        assertNull(Config.avatar(folder.getRoot()));
    }

    @Test public void sampleKeepsTheShortSideAboveTheCircle() {
        assertEquals(1, ProfilePhoto.sampleSize(100, 100));
        assertEquals(1, ProfilePhoto.sampleSize(199, 100));
        assertEquals(2, ProfilePhoto.sampleSize(200, 100));
        assertEquals(16, ProfilePhoto.sampleSize(1500, 72));
    }
}
