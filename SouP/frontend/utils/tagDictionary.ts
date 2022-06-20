export type ITagCategory =
  | "popular"
  | "field"
  | "language"
  | "frontend"
  | "backend"
  | "mobile"
  | "etc"
  | "all";

const _tagDictionary = {
  react: { displayName: "React", color: "#5ed3f3" },
  angular: { displayName: "Angular", color: "#c3002f" },
  vue: { displayName: "Vue", color: "#4eb282" },
  svelte: { displayName: "Svelte", color: "#ff3e00" },
  spring: { displayName: "Spring", color: "#5cb230" },
  nodejs: { displayName: "Node.js", color: "#43733f" },
  go: { displayName: "Go", color: "#00aed8" },
  django: { displayName: "Django", color: "#177852" },
  nestjs: { displayName: "NestJS", color: "#d5214a" },
  express: { displayName: "Express", color: "#bbbbbb" },
  graphql: { displayName: "GraphQL", color: "#de33a6" },
  sql: { displayName: "SQL", color: "#db7533" },
  mongodb: { displayName: "MongoDB", color: "#00ee64" },
  firebase: { displayName: "FireBase", color: "#f2c028" },
  react_native: { displayName: "React Native", color: "#61dafb" },

  aws: { displayName: "AWS", color: "#f29100" },
  docker: { displayName: "Docker", color: "#2496ed" },
  kubernetes: { displayName: "Kubernetes", color: "#3069de" },
  git: { displayName: "Git", color: "#e84d31" },

  typescript: { displayName: "Typescript", color: "#2f74c0" },
  javascript: { displayName: "Javascript", color: "#ead41c" },
  python: { displayName: "Python", color: "#3f7dae" },
  java: { displayName: "Java", color: "#ec2025" },
  kotlin: { displayName: "Kotlin", color: "#c314e2" },
  c_cpp: { displayName: "C/C++", color: "#659ad2" },
  csharp: { displayName: "C#", color: "gray" },
  swift: { displayName: "Swift", color: "#f05138" },
  dart: { displayName: "Dart", color: "#40c3fe" },

  study: { displayName: "스터디", color: "gray" },
  project: { displayName: "프로젝트", color: "gray" },
  planning: { displayName: "기획", color: "gray" },
  service: { displayName: "서비스", color: "gray" },
  code_interview: { displayName: "코테/알고리즘", color: "gray" },
  frontend: { displayName: "프론트엔드", color: "gray" },
  backend: { displayName: "백엔드", color: "gray" },
  mobile: { displayName: "모바일", color: "gray" },
  ios: { displayName: "iOS", color: "#f44336" },
  android: { displayName: "Android", color: "#3ddc84" },
  ui_ux: { displayName: "UI/UX", color: "gray" },
  ai_ml: { displayName: "AI/머신러닝", color: "gray" },
  game: { displayName: "게임", color: "gray" },
  blockchain: { displayName: "블록체인", color: "gray" },
};

export const TagDictionary: Readonly<
  Record<
    keyof typeof _tagDictionary,
    {
      displayName: string;
      color: string;
    }
  >
> = _tagDictionary;

export type ITag = Readonly<keyof typeof TagDictionary>;

export const TagList: ITag[] = Object.keys(TagDictionary) as ITag[];

export const getDisplayColor = (stack: ITag) => {
  return TagDictionary[stack]?.color || stack;
};

export const getDisplayTag = (stack: ITag) => {
  return TagDictionary[stack]?.displayName || stack;
};

export const TagGroup: Readonly<
  {
    displayName: string;
    key: ITagCategory;
    items: ITag[];
  }[]
> = [
  {
    displayName: "인기",
    key: "popular",
    items: [
      "react",
      "angular",
      "vue",
      "spring",
      "python",
      "nodejs",
      "java",
      "typescript",
      "javascript",
    ],
  },
  {
    displayName: "분야",
    key: "field",
    items: [
      "study",
      "project",
      "planning",
      "service",
      "code_interview",
      "frontend",
      "backend",
      "mobile",
      "ui_ux",
      "ai_ml",
      "game",
      "blockchain",
    ],
  },
  {
    displayName: "언어",
    key: "language",
    items: [
      "javascript",
      "typescript",
      "python",
      "java",
      "kotlin",
      "c_cpp",
      "csharp",
      "swift",
      "dart",
    ],
  },
  {
    displayName: "프론트엔드",
    key: "frontend",
    items: [
      "frontend",
      "javascript",
      "typescript",
      "react",
      "vue",
      "angular",
      "svelte",
    ],
  },
  {
    displayName: "백엔드",
    key: "backend",
    items: [
      "backend",
      "java",
      "nodejs",
      "go",
      "python",
      "spring",
      "django",
      "nestjs",
      "express",
      "graphql",
      "sql",
      "mongodb",
      "firebase",
    ],
  },
  {
    displayName: "모바일",
    key: "mobile",
    items: [
      "mobile",
      "ios",
      "android",
      "java",
      "kotlin",
      "swift",
      "dart",
      "react_native",
    ],
  },
  {
    displayName: "기타",
    key: "etc",
    items: ["aws", "kubernetes", "docker", "git"],
  },
  {
    displayName: "전체",
    key: "all",
    items: TagList,
  },
];
