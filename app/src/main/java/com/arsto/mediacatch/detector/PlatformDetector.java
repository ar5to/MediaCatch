package com.arsto.mediacatch.detector;

public class PlatformDetector {

    public static String getPlatformName(String url) {
        if (url == null || url.isEmpty()) return "Unknown";

        if (url.contains("youtube") || url.contains("youtu.be")) return "YouTube";
        if (url.contains("facebook") || url.contains("fb.com") || url.contains("fb.watch")) return "Facebook";
        if (url.contains("instagram")) return "Instagram";
        if (url.contains("terabox")) return "Terabox";
        if (url.contains("twitter") || url.contains("x.com")) return "X (Twitter)";
        if (url.contains("vimeo")) return "Vimeo";
        if (url.contains("tiktok")) return "TikTok";
        if (url.contains("pinterest")) return "Pinterest";
        if (url.contains("linkedin")) return "LinkedIn";
        if (url.contains("tumblr")) return "Tumblr";
        if (url.contains("snapchat") || url.contains("t.snapchat")) return "Snapchat";
        if (url.contains("reddit")) return "Reddit";
        if (url.contains("soundcloud")) return "SoundCloud";
        if (url.contains("spotify")) return "Spotify";
        if (url.contains("twitch")) return "Twitch";
        if (url.contains("mixcloud")) return "Mixcloud";
        if (url.contains("bandcamp")) return "Bandcamp";
        if (url.contains("bitchute")) return "BitChute";
        if (url.contains("rumble")) return "Rumble";
        if (url.contains("vk.com")) return "VK";
        if (url.contains("odnoklassniki")) return "Odnoklassniki";
        if (url.contains("streamable")) return "Streamable";
        if (url.contains("imgur")) return "Imgur";
        if (url.contains("9gag")) return "9GAG";
        if (url.contains("gfycat")) return "Gfycat";
        if (url.contains("flickr")) return "Flickr";
        if (url.contains("ted.com")) return "TED";
        if (url.contains("archive.org")) return "Internet Archive";
        if (url.contains("threads")) return "Threads";
        if (url.contains("yandex")) return "Yandex Video";
        if (url.contains("kuaishou")) return "Kuaishou";
        if (url.contains("wistia")) return "Wistia";

        return "Unknown";
    }
}
