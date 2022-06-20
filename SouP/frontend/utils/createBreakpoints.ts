type Orientation = "landscape" | "portrait";

/*
  xs: "0px",
  sm: "576px",
  md: "768px",
  lg: "992px",
  xl: "1200px",
*/

const defaultBreakpoints = {
  sm: 0,
  md: 768,
  lg: 1024,
};

export interface Breakpoints {
  greaterThanOrEqual: (
    min: keyof typeof defaultBreakpoints,
    orientation?: Orientation
  ) => any;
  greaterThan: (
    min: keyof typeof defaultBreakpoints,
    orientation?: Orientation
  ) => any;
  lessThan: (
    max: keyof typeof defaultBreakpoints,
    orientation?: Orientation
  ) => string;
  between: (
    min: keyof typeof defaultBreakpoints,
    max: keyof typeof defaultBreakpoints,
    orientation?: Orientation
  ) => string;
  at: (
    name: keyof typeof defaultBreakpoints,
    orientation?: Orientation
  ) => string;
}

const getRelativeBreakpoint = (
  breakPoint: keyof typeof defaultBreakpoints,
  offset: number
) => {
  const breakPoints = Object.keys(
    defaultBreakpoints
  ) as (keyof typeof defaultBreakpoints)[];
  const index = breakPoints.indexOf(breakPoint);
  if (
    index === -1 ||
    index + offset <= 0 ||
    index + offset >= breakPoints.length
  )
    return breakPoint;
  return breakPoints[index + offset];
};

export function createBreakpoints(options = defaultBreakpoints): Breakpoints {
  const withBreakpoints = (
    fn: (a: (b: keyof typeof defaultBreakpoints) => number) => string
  ) => fn((val) => options[val]);

  const withOrientation = (mediaQuery: string, orientation: Orientation) => {
    return `${mediaQuery} and (orientation: ${orientation})`;
  };

  const withOrientationOrNot = (result: string, orientation?: Orientation) =>
    orientation ? withOrientation(result, orientation) : result;

  const withMinAndMaxMedia = (min: number, max: number) =>
    `@media (min-width: ${min}px) and (max-width: ${max}px)`;

  return {
    greaterThan: (name, orientation) =>
      withBreakpoints((bp) =>
        withOrientationOrNot(`@media (min-width: ${bp(name)}px)`, orientation)
      ),
    greaterThanOrEqual: (name, orientation) =>
      withBreakpoints((bp) =>
        withOrientationOrNot(
          `@media (min-width: ${bp(getRelativeBreakpoint(name, 1))}px)`,
          orientation
        )
      ),
    lessThan: (name, orientation) =>
      withBreakpoints((bp) =>
        withOrientationOrNot(
          `@media (max-width: ${bp(name) - 1}px)`,
          orientation
        )
      ),
    between: (min, max, orientation) =>
      withBreakpoints((bp) =>
        withOrientationOrNot(
          withMinAndMaxMedia(bp(min), bp(max) - 1),
          orientation
        )
      ),
    at: (name, orientation) =>
      withBreakpoints((bp) =>
        withOrientationOrNot(
          withMinAndMaxMedia(bp(name), bp(getRelativeBreakpoint(name, 1)) - 1),
          orientation
        )
      ),
  };
}

export const breakpoints = createBreakpoints();
