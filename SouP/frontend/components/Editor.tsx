import { css } from "@emotion/react";
import styled from "@emotion/styled";
import {
  useEditor,
  EditorContent,
  Editor as TipTapEditor,
  JSONContent,
  Content,
} from "@tiptap/react";
import StarterKit from "@tiptap/starter-kit";
import Image from "@tiptap/extension-image";
import { Flex, Box } from "common/components";
import {
  MdOutlineCode,
  MdOutlineFormatBold,
  MdOutlineFormatClear,
  MdOutlineFormatItalic,
  MdOutlineFormatListBulleted,
  MdOutlineFormatListNumbered,
  MdOutlineFormatQuote,
  MdOutlineFormatStrikethrough,
  MdOutlineHorizontalRule,
  MdOutlineImage,
  MdOutlineLooksOne,
  MdOutlineLooksTwo,
  MdOutlineRedo,
  MdOutlineSmartDisplay,
  MdOutlineSplitscreen,
  MdOutlineUndo,
} from "react-icons/md";
import { Youtube } from "tiptap/youtube";
import { UseFormSetValue } from "react-hook-form";
import { useEffect } from "react";
import { IArticleData } from "pages/projects/write";

const ButtonElem = styled.button<{
  active?: boolean;
}>`
  border-radius: 4px;
  padding: 6px;
  :disabled {
    color: var(--outline);
  }
  // border: 1px solid var(--outline);
  ${({ active }) =>
    active &&
    css`
      background-color: var(--background);
      outline: 1px solid var(--outline);
    `};
`;

const ButtonGroup = styled(Flex)`
  font-size: 14px;
  border-radius: 4px;
  // padding: 4px;
  // border: 1px solid var(--outline);
`;

const Button = ({
  onMouseDown,
  ...props
}: React.ComponentProps<typeof ButtonElem>) => {
  return (
    <ButtonElem
      type="button"
      onMouseDown={(e) => {
        e.preventDefault();
        if (onMouseDown) onMouseDown(e);
      }}
      {...props}
    />
  );
};

const MenuBar = ({ editor }: { editor: TipTapEditor | null }) => {
  if (!editor) {
    return null;
  }

  const addImage = () => {
    const url = window.prompt("URL");

    if (url) {
      editor.chain().focus().setImage({ src: url }).run();
    }
  };

  const addYoutube = () => {
    const url = window.prompt("Youtube Link");

    if (!url) return;
    const regex =
      /(?:.+)(?:\.be\/|v=)([a-zA-Z0-9|_|-]+?)(?:&|$)(?:t=)?(\d+m\ds|\d+)?/;
    const [_, v, time] = url?.match(regex) || [null, null];
    if (!v) return;

    if (url) {
      editor
        .chain()
        .focus()
        .setYoutube({
          src: `https://www.youtube.com/embed/${v}${
            time ? `start${time}` : ""
          }`,
        })
        .run();
    }
  };

  return (
    <Flex
      css={{
        flexWrap: "wrap",
        rowGap: "8px",
        "& > *:not(:last-child)": {
          marginRight: "12px",
        },
        " & > * > *:not(:last-child)": {
          marginRight: "4px",
        },
      }}
    >
      <ButtonGroup>
        <Button onClick={addImage}>
          <MdOutlineImage />
        </Button>
        <Button onClick={addYoutube}>
          <MdOutlineSmartDisplay />
        </Button>
      </ButtonGroup>
      <ButtonGroup>
        <Button
          onClick={() => editor.chain().focus().toggleBold().run()}
          active={editor.isActive("bold")}
        >
          <MdOutlineFormatBold />
        </Button>
        <Button
          onClick={() => editor.chain().focus().toggleItalic().run()}
          active={editor.isActive("italic")}
        >
          <MdOutlineFormatItalic />
        </Button>
        <Button
          onClick={() => editor.chain().focus().toggleStrike().run()}
          active={editor.isActive("strike")}
        >
          <MdOutlineFormatStrikethrough />
        </Button>
        <Button onClick={() => editor.chain().focus().unsetAllMarks().run()}>
          <MdOutlineFormatClear />
        </Button>
      </ButtonGroup>
      <ButtonGroup>
        <Button
          onClick={() =>
            editor.chain().focus().toggleHeading({ level: 1 }).run()
          }
          active={editor.isActive("heading", { level: 1 })}
        >
          <MdOutlineLooksOne />
        </Button>
        <Button
          onClick={() =>
            editor.chain().focus().toggleHeading({ level: 2 }).run()
          }
          active={editor.isActive("heading", { level: 2 })}
        >
          <MdOutlineLooksTwo />
        </Button>
      </ButtonGroup>
      {/*     
      <Button
        onClick={() => editor.chain().focus().toggleHeading({ level: 3 }).run()}
        active={editor.isActive("heading", { level: 3 })}
      >
        <MdOutlineLooks3 />
      </Button> 
      <Button onClick={() => editor.chain().focus().clearNodes().run()}>
        clear nodes
      </Button>
      <Button
        onClick={() => editor.chain().focus().toggleCode().run()}
        active={editor.isActive("code")}
      >
        <MdOutlineCode />
      </Button>
      <Button
        onClick={() => editor.chain().focus().setParagraph().run()}
        active={editor.isActive("paragraph")}
      >
        paragraph
      </Button><Button
        onClick={() => editor.chain().focus().toggleHeading({ level: 4 }).run()}
        active={editor.isActive("heading", { level: 4 })}
      >
        h4
      </Button>
      <Button
        onClick={() => editor.chain().focus().toggleHeading({ level: 5 }).run()}
        active={editor.isActive("heading", { level: 5 })}
      >
        h5
      </Button>
      <Button
        onClick={() => editor.chain().focus().toggleHeading({ level: 6 }).run()}
        active={editor.isActive("heading", { level: 6 })}
      >
        h6
      </Button> */}
      <ButtonGroup>
        <Button
          onClick={() => editor.chain().focus().toggleBulletList().run()}
          active={editor.isActive("bulletList")}
        >
          <MdOutlineFormatListBulleted />
        </Button>
        <Button
          onClick={() => editor.chain().focus().toggleOrderedList().run()}
          active={editor.isActive("orderedList")}
        >
          <MdOutlineFormatListNumbered />
        </Button>
        <Button
          onClick={() => editor.chain().focus().toggleCodeBlock().run()}
          active={editor.isActive("codeBlock")}
        >
          <MdOutlineCode />
        </Button>
        <Button
          onClick={() => editor.chain().focus().toggleBlockquote().run()}
          active={editor.isActive("blockquote")}
        >
          <MdOutlineFormatQuote />
        </Button>
        <Button
          onClick={() => editor.chain().focus().setHorizontalRule().run()}
        >
          <MdOutlineHorizontalRule />
        </Button>
      </ButtonGroup>
      <ButtonGroup>
        <Button onClick={() => editor.chain().focus().setHardBreak().run()}>
          <MdOutlineSplitscreen />
        </Button>
        <Button
          onClick={() => editor.chain().focus().undo().run()}
          disabled={!editor.can().undo()}
        >
          <MdOutlineUndo />
        </Button>
        <Button
          onClick={() => editor.chain().focus().redo().run()}
          disabled={!editor.can().redo()}
        >
          <MdOutlineRedo />
        </Button>
      </ButtonGroup>
    </Flex>
  );
};

export const Editor = ({
  setValue,
  initialContent,
}: {
  setValue: UseFormSetValue<IArticleData>;
  initialContent?: Content;
}) => {
  const editor = useEditor({
    onBlur({ editor }) {
      setValue("content", editor?.getJSON());
    },
    extensions: [StarterKit, Image, Youtube],
    parseOptions: {
      preserveWhitespace: "full",
    },
    content: initialContent,
    editorProps: {
      attributes: {
        autocomplete: "off",
        autocorrect: "off",
        autocapitalize: "off",
        spellcheck: "false",
      },
    },
  });

  useEffect(() => {
    if (editor) setValue("content", editor?.getJSON());
  }, [setValue, editor]);

  return (
    <>
      <Flex
        column
        css={{
          ".ProseMirror": {
            minHeight: "600px",
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
      >
        <div
          css={{
            position: "sticky",
            top: "59px",
            zIndex: 1,
            borderBottom: "1px solid var(--outline)",
            padding: "12px 0px",
            marginTop: "-12px",
            marginBottom: "12px",
          }}
        >
          <div
            css={{
              marginTop: "-12px",
              position: "absolute",
              backgroundColor: "var(--positive)",
              width: "100%",
              height: "50px",
              opacity: 0.95,
              zIndex: -1,
            }}
          />
          <MenuBar editor={editor} />
        </div>
        <EditorContent css={{ padding: "0px 4px" }} editor={editor} />
      </Flex>
      {/* <div css={{ whiteSpace: "break-spaces" }}>
        {JSON.stringify(editor?.getText())}
      </div> */}
    </>
  );
};
