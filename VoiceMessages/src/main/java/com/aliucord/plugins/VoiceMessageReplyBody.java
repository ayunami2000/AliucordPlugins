package com.aliucord.plugins;

import com.discord.models.domain.NonceGenerator;
import com.discord.utilities.time.ClockFactory;

import java.util.ArrayList;
import java.util.List;

public class VoiceMessageReplyBody {

    String content = "";
    Long channel_id;
    int type = 0;
    int flags = 8192;
    String nonce = String.valueOf(NonceGenerator.computeNonce(ClockFactory.get()));
    List<Attachment> attachments = new ArrayList<>();
    MessageReference message_reference;

    public VoiceMessageReplyBody(Long channel_id, Attachment attachment, Long reply_channel_id, Long guild_id, Long reply_id) {
        this.channel_id = channel_id;
        this.attachments.add(attachment);
        this.message_reference = new MessageReference(reply_channel_id.toString(), guild_id.toString(), reply_id.toString());
    }

    public static class Attachment {
        String id = "0";
        String filename;
        String uploaded_filename;
        float duration_secs;
        String waveform;

        public Attachment(String filename, String uploaded_filename, float duration_secs, String waveform) {
            this.filename = filename;
            this.uploaded_filename = uploaded_filename;
            this.duration_secs = duration_secs;
            this.waveform = waveform;
        }
    }

    public static class MessageReference {
        // ,"message_reference":{"guild_id":"811255666990907402","channel_id":"811261478875299840","message_id":"1205924577964986510"}
        String guild_id;
        String channel_id;
        String message_id;

        public MessageReference(String guild_id, String channel_id, String message_id) {
            this.guild_id = guild_id;
            this.channel_id = channel_id;
            this.message_id = message_id;
        }
    }
}

