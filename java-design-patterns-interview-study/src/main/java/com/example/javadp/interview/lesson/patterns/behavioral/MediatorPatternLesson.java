package com.example.javadp.interview.lesson.patterns.behavioral;

import com.example.javadp.interview.lesson.PatternLessonHeader;
import com.example.javadp.interview.study.StudyContext;

/**
 * GoF Mediator: route interactions through a hub so colleagues avoid tight N-squared coupling.
 */
public final class MediatorPatternLesson {

    private MediatorPatternLesson() {}

    public static void run(StudyContext ctx) {
        PatternLessonHeader.printRealWorldExample(
                ctx,
                1,
                "JavaFX / Swing property bindings",
                "UI frameworks mediate updates so widgets react without each widget storing direct references to all peers.",
                "Looser than classic GoF sample but same decoupling story.");
        PatternLessonHeader.printRealWorldExample(
                ctx,
                2,
                "Enterprise Service Bus message brokers",
                "Topics and queues mediate between publishers and subscribers; endpoints do not name every consumer.",
                "Hub-and-spoke integration vs point-to-point spaghetti.");
        PatternLessonHeader.printRealWorldExample(
                ctx,
                3,
                "Air traffic control",
                "Planes coordinate through a mediator (tower), not by each plane broadcasting to all others.",
                "Classic analogy interviewers still recognize.");

        ctx.log("--- Java core tie-in ---");
        java.net.ProxySelector selector = java.net.ProxySelector.getDefault();
        ctx.log("java.net.ProxySelector routes URIs to proxies (JDK mediator for HTTP stacks): "
                + (selector != null ? selector.getClass().getName() : "null"));

        PatternLessonHeader.print(
                ctx,
                "Mediator",
                "GoF Behavioral",
                "Hub routes messages so colleagues do not reference each other directly.",
                "ChatRoom forwards alice.send to bob without Alice holding a direct Bob field.");
        ChatRoom room = new ChatRoom();
        Chatter alice = new Chatter("alice", room);
        Chatter bob = new Chatter("bob", room);
        alice.send(ctx, "bob", "hello");
        bob.send(ctx, "alice", "hi back");
    }

    private static final class ChatRoom {
        private final java.util.Map<String, Chatter> members = new java.util.HashMap<>();

        void join(Chatter c) {
            members.put(c.name(), c);
        }

        void route(StudyContext ctx, String from, String to, String text) {
            Chatter target = members.get(to);
            if (target == null) {
                ctx.log("No such user: " + to);
                return;
            }
            target.receive(ctx, from, text);
        }
    }

    private static final class Chatter {
        private final String name;
        private final ChatRoom room;

        Chatter(String name, ChatRoom room) {
            this.name = name;
            this.room = room;
            room.join(this);
        }

        String name() {
            return name;
        }

        void send(StudyContext ctx, String to, String text) {
            room.route(ctx, name, to, text);
        }

        void receive(StudyContext ctx, String from, String text) {
            ctx.log(name + " received from " + from + ": " + text);
        }
    }
}
