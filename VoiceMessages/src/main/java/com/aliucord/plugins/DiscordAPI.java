package com.aliucord.plugins;

import com.aliucord.Http;
import com.aliucord.utils.GsonUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import kotlin.io.FilesKt;

public class DiscordAPI {

    public static String uploadFile(File file, long channel, String extension) {
        try {
            JSONObject jsonResponse;
            try (Http.Request req = Http.Request.newDiscordRNRequest("/channels/" + channel + "/attachments", "POST")
                    .setHeader("content-type", "application/json")
                    .setHeader("x-discord-locale", "en-US")) {

                AttachmentBody body = new AttachmentBody(file.getName(), (int) file.length());

                Http.Response response = req.executeWithBody(GsonUtils.toJson(GsonUtils.getGson(), body));

                if (response.statusCode != 200) {
                    throw new RuntimeException("Failed to upload file: " + response.statusCode + " " + response.text());
                }
                jsonResponse = new JSONObject(response.text());
            }

            JSONObject attachment = jsonResponse.getJSONArray("attachments").getJSONObject(0);

            String type = extension.equals(".ogg") ? "audio/ogg" : "audio/x-aac";
            try (Http.Request uploadReq = new Http.Request(attachment.getString("upload_url")).setHeader("Content-Type", type)
                    .setHeader("Content-Length", file.length() + "")
                    .setHeader("user-agent", "Discord-Android/175207;RNA")) {

                uploadReq.conn.setRequestMethod("PUT");
                Http.Response upload = uploadReq.executeWithBody(FilesKt.readBytes(file));
                if (upload.statusCode != 200) {
                    throw new RuntimeException("Failed to upload file: " + upload.statusCode + " " + upload.text());
                }
            }
            return attachment.getString("upload_filename");
        } catch (IOException | JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public static void sendVoiceMessage(String fileName, float duration, String waveform, long channelID, long replyChannelID, long replyID, long guildID, String extension) {
        try {
            try (Http.Request request = Http.Request.newDiscordRNRequest("/channels/" + channelID + "/messages", "POST")) {
                request.setHeader("content-type", "application/json");

                // ,"message_reference":{"guild_id":"811255666990907402","channel_id":"811261478875299840","message_id":"1205924577964986510"}
                if (replyID != 0) {
                    VoiceMessageReplyBody body = new VoiceMessageReplyBody(channelID, new VoiceMessageReplyBody.Attachment(
                            "voice-message" + extension,
                            fileName,
                            duration,
                            waveform
                    ), replyChannelID, guildID, replyID);
                    request.executeWithBody(GsonUtils.toJson(GsonUtils.getGson(), body));
                } else {
                    VoiceMessageBody body = new VoiceMessageBody(channelID, new VoiceMessageBody.Attachment(
                            "voice-message" + extension,
                            fileName,
                            duration,
                            waveform
                    ));
                    request.executeWithBody(GsonUtils.toJson(GsonUtils.getGson(), body));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
