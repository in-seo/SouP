import { css, Theme } from "@emotion/react";
import styled from "@emotion/styled";
import { LabelVariant, LabelSize } from "./Label";

const ButtonStyle = css`
  line-height: normal;
  color: inherit;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 600;
  &:hover {
    filter: brightness(0.98);
  }
  &:disabled {
    filter: opacity(0.5);
  }
`;

export const Button = styled.button<{
  variant?: keyof ReturnType<typeof LabelVariant>;
  size?: keyof ReturnType<typeof LabelSize>;
  icon?: boolean;
}>`
  ${ButtonStyle}
  ${({ size }) => (size ? LabelSize()[size] : LabelSize()["default"])}
  ${({ variant, theme }) =>
    variant ? LabelVariant(theme)[variant] : LabelVariant(theme)["default"]}
  ${({ icon }) => icon || css``}
`;

export const ButtonLink = styled.a<{
  variant?: keyof ReturnType<typeof LabelVariant>;
  size?: keyof ReturnType<typeof LabelSize>;
  icon?: boolean;
}>`
  appearance: button;
  ${ButtonStyle}
  ${({ icon }) =>
    icon ||
    css`
      padding: 0;
      svg {
        margin-left: -0.1em;
        margin-right: 0.3em;
      }
    `}
  ${({ size }) => (size ? LabelSize()[size] : LabelSize()["default"])}
  ${({ variant, theme }) =>
    variant ? LabelVariant(theme)[variant] : LabelVariant(theme)["default"]}
`;
