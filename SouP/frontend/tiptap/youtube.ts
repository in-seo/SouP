import { Node, mergeAttributes } from "@tiptap/core";

export interface YoutubeOptions {
  HTMLAttributes: Record<string, any>;
}

declare module "@tiptap/core" {
  interface Commands<ReturnType> {
    youtube: {
      /**
       * Add an youtube video
       */
      setYoutube: (options: { src: string }) => ReturnType;
    };
  }
}

export const Youtube = Node.create<YoutubeOptions>({
  name: "youtube",

  priority: 1000,

  addOptions() {
    return {
      HTMLAttributes: {},
    };
  },

  addAttributes() {
    return {
      src: {
        default: null,
      },
    };
  },

  group: "block",

  content: "inline*",

  marks: "",

  parseHTML() {
    return [
      {
        tag: "youtube[src]",
      },
    ];
  },

  renderHTML({ HTMLAttributes }) {
    return [
      "iframe",
      mergeAttributes(
        {
          src: `https://www.youtube.com/embed/${this.options.HTMLAttributes["src"]}`,
          title: "YouTube video player",
          frameBorder: "0",
          allow:
            "accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture",
          allowFullScreen: "true",
        },
        HTMLAttributes
      ),
      0,
    ];
  },

  addCommands() {
    return {
      setYoutube:
        (options) =>
        ({ commands }) => {
          return commands.insertContent({
            type: this.name,
            attrs: options,
          });
        },
    };
  },
});
