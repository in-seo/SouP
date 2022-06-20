export const toMatrix = <T,>(arr: T[], width: number) =>
  arr.reduce((rows, key, index) => {
    index % width == 0 ? rows.push([key]) : rows[rows.length - 1].push(key);
    return rows;
  }, [] as T[][]);
