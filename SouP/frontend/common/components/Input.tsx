import { css } from "@emotion/react";
import styled from "@emotion/styled";
import TextareaAutosize from "react-textarea-autosize";
import { LabelVariant } from "common/components";

const SizeVariant = () => ({
  smaller: css`
    min-height: 22px;
    padding: 0px 6px;
    font-size: 11px;
    border-radius: 4px;
  `,
  small: css`
    min-height: 28px;
    padding: 0px 8px;
    font-size: 12px;
    border-radius: 6px;
  `,
});

export const Input = styled.input<{
  variant?: keyof ReturnType<typeof LabelVariant>;
  size?: keyof ReturnType<typeof SizeVariant>;
}>`
  line-height: normal;
  padding: 0px 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  :focus {
    outline: 2px solid var(--primarylight);
  }
  ${({ size }) =>
    size
      ? SizeVariant[size]
      : css`
          height: 36px;
          padding: 0px 16px;
          border-radius: 8px;
        `}

  ${({ variant, theme }) =>
    variant
      ? LabelVariant(theme)[variant]
      : css`
          border: 1px solid var(--outline);
          background-color: var(--positive);
        `}
`;

export const TextArea = styled(TextareaAutosize)<{
  variant?: keyof typeof LabelVariant;
  size?: keyof typeof SizeVariant;
}>`
  resize: none;
  display: flex;
  align-items: center;
  justify-content: center;
  :focus {
    outline: 2px solid var(--primarylight);
  }
  ${({ size }) =>
    size
      ? SizeVariant[size]
      : css`
          height: 36px;
          padding: 8px 16px;
          border-radius: 8px;
        `}

  ${({ variant }) =>
    variant
      ? LabelVariant[variant]
      : css`
          border: 1px solid var(--outline);
          background-color: var(--positive);
        `}
`;
