import { css, Theme } from "@emotion/react";
import styled from "@emotion/styled";

export const LabelVariant = (theme: Theme) => ({
  default: css`
    border: 0px;
    background-color: var(--positive1);
  `,
  primary: css`
    color: var(--positive);
    border: 0px;
    background-color: var(--primary);
  `,
  "primary-outlined": css`
    color: var(--primary);
    border: 1px solid var(--primary);
    background-color: var(--positive);
  `,
  white: css`
    border: 1px solid var(--outline);
    background-color: var(--positive);
  `,
  background: css`
    background-color: var(--background);
  `,
  transparent: css`
    border: 0px;
    background-color: transparent;
  `,
});

export const LabelSize = () => ({
  default: css`
    height: 36px;
    padding: 0px 12px;
    border-radius: 8px;
  `,
  smaller: css`
    min-height: 22px;
    padding: 0px 8px;
    font-size: 11px;
    border-radius: 4px;
  `,
  small: css`
    min-height: 28px;
    padding: 0px 10px;
    font-size: 12px;
    border-radius: 6px;
  `,
  freeform: css`
    border-radius: 8px;
  `,
});

export const Label = styled.div<{
  variant?: keyof ReturnType<typeof LabelVariant>;
  size?: keyof ReturnType<typeof LabelSize>;
}>`
  line-height: normal;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 600;
  ${({ size }) => (size ? LabelSize()[size] : LabelSize()["default"])}
  ${({ variant, theme }) =>
    variant ? LabelVariant(theme)[variant] : LabelVariant(theme)["default"]}
`;
