import { useEditor, EditorContent, JSONContent } from "@tiptap/react";
import StarterKit from "@tiptap/starter-kit";
import Image from "@tiptap/extension-image";
import { Youtube } from "tiptap/youtube";

export const Viewer = ({
  content,
  ...props
}: Omit<React.HTMLProps<HTMLDivElement>, "content"> & {
  content: JSONContent;
}) => {
  const editor = useEditor({
    editable: false,
    extensions: [StarterKit, Image, Youtube],
    parseOptions: {
      preserveWhitespace: "full",
    },
    content: content || {
      type: "doc",
      content: [
        {
          type: "heading",
          attrs: { level: 1 },
          content: [{ type: "text", text: "없으면 인류의 얼음이 봄바람이다." }],
        },
        {
          type: "heading",
          attrs: { level: 2 },
          content: [{ type: "text", text: "열락의 수 구하기 목숨을 듣는다. " }],
        },
        {
          type: "paragraph",
          content: [
            {
              type: "text",
              text: "인생에 하였으며, 곳으로 우리 청춘의 보내는 쓸쓸하랴? 있는 없으면, 소담스러운 위하여 쓸쓸하랴? 아니한 없는 가슴에 얼음에 반짝이는 같으며, 밥을 길을 찾아다녀도, 힘있다. 풀밭에 희망의 곳이 위하여서 그들은 이것을 역사를 황금시대다. 트고, 물방아 인도하겠다는 있으며, 아니한 있음으로써 이것이다. 설산에서 사랑의 따뜻한 것은 아름답고 있으랴? 피가 하였으며, 인생의 우리 이 것이다.",
            },
          ],
        },
        {
          type: "codeBlock",
          attrs: { language: null },
          content: [
            {
              type: "text",
              text: 'editorProps: {\n  attributes: {\n    autocomplete: "off",\n    autocorrect: "off",\n    autocapitalize: "off",\n    spellcheck: "false",\n  },\n}',
            },
          ],
        },
        {
          type: "paragraph",
          content: [
            {
              type: "text",
              text: "있을 오아이스도 인간은 안고, 대고, 과실이 별과 물방아 힘있다. 그들의 청춘에서만 이상은 예가 보배를 갑 하는 때문이다. 충분히 이 얼음이 그들은 피는 것은 이상의 부패뿐이다. 이것을 눈에 현저하게 석가는 얼음이 것이다. 있음으로써 끓는 구할 풀이 바로 자신과 피고, 두손을 이것이다. 구할 이것을 풍부하게 주며, 못할 봄날의 피가 그러므로 피다. 인간의 과실이 청춘은 설레는 작고 뼈 이 풀밭에 트고, 이것이다. 웅대한 그러므로 꽃이 만물은 사막이다. 꾸며 청춘의 이것은 능히 이상은 내는 아니더면, 봄바람이다.",
            },
          ],
        },
        {
          type: "youtube",
          attrs: { src: "https://www.youtube.com/embed/njX2bu-_Vw4" },
        },
        {
          type: "paragraph",
          content: [
            {
              type: "text",
              text: "얼마나 관현악이며, 봄바람을 위하여서. 인생에 위하여 시들어 영락과 그들의 되려니와, 기쁘며, 이것이다. 관현악이며, 얼마나 청춘을 사막이다. 뜨고, 놀이 용감하고 위하여서. 전인 대한 작고 실로 미묘한 이상을 보는 든 것이다. 할지니, 긴지라 착목한는 열락의 이것을 방황하여도, 사막이다. 끓는 앞이 이는 가슴이 곧 것이다. 때까지 창공에 튼튼하며, 끓는 싹이 그러므로 청춘이 피다. 튼튼하며, 시들어 꽃이 따뜻한 들어 열락의 용기가 인간이 방황하여도, 있다. 하여도 지혜는 눈에 평화스러운 생생하며, 바이며, 모래뿐일 그리하였는가? 인간은 있는 목숨이 피고, 앞이 내는 것이다.",
            },
          ],
        },
        { type: "paragraph" },
        {
          type: "heading",
          attrs: { level: 1 },
          content: [{ type: "text", text: "없으면 인류의 얼음이 봄바람이다." }],
        },
        {
          type: "heading",
          attrs: { level: 2 },
          content: [{ type: "text", text: "열락의 수 구하기 목숨을 듣는다. " }],
        },
        {
          type: "paragraph",
          content: [
            {
              type: "text",
              text: "열락의 수 구하기 목숨을 듣는다. 인생에 하였으며, 곳으로 우리 청춘의 보내는 쓸쓸하랴? 있는 없으면, 소담스러운 위하여 쓸쓸하랴? 아니한 없는 가슴에 얼음에 반짝이는 같으며, 밥을 길을 찾아다녀도, 힘있다. 풀밭에 희망의 곳이 위하여서 그들은 이것을 역사를 황금시대다. 트고, 물방아 인도하겠다는 있으며, 아니한 있음으로써 이것이다. 설산에서 사랑의 따뜻한 것은 아름답고 있으랴? 피가 하였으며, 인생의 우리 이 것이다.",
            },
          ],
        },
        {
          type: "paragraph",
          content: [
            {
              type: "text",
              text: "있을 오아이스도 인간은 안고, 대고, 과실이 별과 물방아 힘있다. 그들의 청춘에서만 이상은 예가 보배를 갑 하는 때문이다. 충분히 이 얼음이 그들은 피는 것은 이상의 부패뿐이다. 이것을 눈에 현저하게 석가는 얼음이 것이다. 있음으로써 끓는 구할 풀이 바로 자신과 피고, 두손을 이것이다. 구할 이것을 풍부하게 주며, 못할 봄날의 피가 그러므로 피다. 인간의 과실이 청춘은 설레는 작고 뼈 이 풀밭에 트고, 이것이다. 웅대한 그러므로 꽃이 만물은 사막이다. 꾸며 청춘의 이것은 능히 이상은 내는 아니더면, 봄바람이다.",
            },
          ],
        },
        {
          type: "paragraph",
          content: [
            {
              type: "text",
              text: "얼마나 관현악이며, 봄바람을 위하여서. 인생에 위하여 시들어 영락과 그들의 되려니와, 기쁘며, 이것이다. 관현악이며, 얼마나 청춘을 사막이다. 뜨고, 놀이 용감하고 위하여서. 전인 대한 작고 실로 미묘한 이상을 보는 든 것이다. 할지니, 긴지라 착목한는 열락의 이것을 방황하여도, 사막이다. 끓는 앞이 이는 가슴이 곧 것이다. 때까지 창공에 튼튼하며, 끓는 싹이 그러므로 청춘이 피다. 튼튼하며, 시들어 꽃이 따뜻한 들어 열락의 용기가 인간이 방황하여도, 있다. 하여도 지혜는 눈에 평화스러운 생생하며, 바이며, 모래뿐일 그리하였는가? 인간은 있는 목숨이 피고, 앞이 내는 것이다.",
            },
          ],
        },
      ],
    },
    editorProps: {
      attributes: {
        autocomplete: "off",
        autocorrect: "off",
        autocapitalize: "off",
        spellcheck: "false",
      },
    },
  });

  return (
    <div
      css={{
        ".ProseMirror": {
          ":focus": {
            outline: "none",
          },
          "& > *": {
            whiteSpace: "break-spaces",
          },
          lineHeight: 1.5,
          p: {
            marginBlockEnd: "6px",
          },
          h1: {
            fontSize: "24px",
            fontWeight: "600",
            lineHeight: 2,
          },
          h2: {
            fontSize: "18px",
            fontWeight: "600",
            lineHeight: 2,
          },
          pre: {
            padding: "8px",
            backgroundColor: "var(--background)",
            border: "1px solid var(--outline)",
            marginBlockStart: "12px",
            marginBlockEnd: "12px",
            borderRadius: "4px",
          },
          iframe: {
            marginBlockStart: "18px",
            marginBlockEnd: "18px",
            borderRadius: "8px",
            backgroundColor: "var(--outline)",
            width: "100%",
            maxWidth: "560px",
            height: "50vw",
            maxHeight: "315px",
          },
        },
      }}
      {...props}
    >
      <EditorContent editor={editor} />
    </div>
  );
};
