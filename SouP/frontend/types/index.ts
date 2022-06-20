import { JSONContent } from "@tiptap/react";
import { NextPage } from "next";
import { AppProps } from "next/app";
import { ITag } from "utils/tagDictionary";

export interface IPageable<T> {
  content: T;
  pageable: {
    sort: { empty: true; sorted: false; unsorted: true };
    offset: number;
    pageNumber: number;
    pageSize: number;
    unpaged: boolean;
    paged: boolean;
  };
  last: boolean;
  totalPages: number;
  totalElements: number;
  size: number;
  number: number;
  sort: { empty: boolean; sorted: boolean; unsorted: boolean };
  first: boolean;
  numberOfElements: number;
  empty: boolean;
}

export interface IPost {
  content: string;
  date: string;
  end: boolean;
  id: number;
  link: string;
  postId: number;
  postName: string;
  stacks: ITag[];
  talk: string;
  userName: string;
  views: number;
  source: "SOUP" | "INFLEARN" | "OKKY" | "CAMPICK" | "HOLA";
  fav: number;
}

export interface IProjectContentData<T> {
  id: number;
  postName: string;
  content: T;
  userName: string;
  date: string;
  link: "https://okky.kr/article/1221052";
  stacks: ITag[];
  views: number;
  talk: string;
  source: string;
  fav: number;
  isfav: boolean;
  userId: null | number;
}

export type IProjectData =
  | ({ type: "string" } & IProjectContentData<string>)
  | ({
      type: "prosemirror";
    } & IProjectContentData<JSONContent>);

export type CustomNextPage = NextPage & {
  getLayout?: (page: React.ReactElement) => React.ReactNode;
  authorized?: boolean;
};

export type CustomAppProps = AppProps & {
  Component: CustomNextPage;
};

export type IAuthData =
  | {
      success: true;
      user_id: number;
      profileImage: string;
      username: string;
    }
  | {
      success: false;
    };

export interface ISideBarProps {
  exact?: boolean;
  selected?: boolean;
  authorized?: boolean;
}

export interface IPostPreviewContent {
  id: number;
  postName: string;
  content: string;
  userName: string;
  date: string;
  link: "https://okky.kr/article/1221052";
  stacks: ITag[];
  views: number;
  talk: string;
  source: string;
  fav: number;
  isfav: boolean;
}

export interface ILoungePost {
  date: string;
  user_id: number;
  fav: number;
  lounge_id: number;
  isfav: boolean;
  picture: string;
  content: string;
  username: string;
}

export interface IArticleData {
  title: string;
  content: JSONContent;
}

export interface IFeaturedItem {
  title: string;
  userName: string;
  id: number;
}
