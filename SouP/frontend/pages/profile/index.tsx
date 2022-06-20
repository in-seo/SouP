import Image from "next/image";
import {
  Flex,
  Button,
  TextArea,
  Input,
  Box,
  SectionHeader,
  SectionBody,
  Hr,
} from "common/components";
import { ChildrenContainer } from "components";
import { useToggle } from "hooks/useToggle";
import { MdOutlineEdit } from "react-icons/md";
import useAuth from "hooks/useAuth";
import { useQuery } from "react-query";
import { http } from "common/services";
import { CustomNextPage } from "types";

const DetailsRow = ({
  item,
  value,
  index,
}: {
  item: string;
  value: string;
  index: number;
}) => (
  <>
    <p css={{ gridRow: index, gridColumn: 1, fontWeight: 600 }}>{item}</p>
    <p css={{ gridRow: index, gridColumn: 2 }}>{value}</p>
  </>
);

const InfoEdit = ({ toggleIsEdit }: { toggleIsEdit: () => void }) => {
  return (
    <ChildrenContainer>
      <Flex
        css={{
          alignItems: "center",
          padding: "6px",
          marginBottom: "12px",
        }}
      >
        <div
          css={{
            overflow: "hidden",
            borderRadius: "9999px",
            height: "72px",
            width: "72px",
          }}
        >
          <Image
            src="https://i.imgur.com/tvzwhsF.png"
            alt="me"
            width="96px"
            height="96px"
          />
        </div>
        <div css={{ marginLeft: "16px", flex: "1" }}>
          <p css={{ fontSize: "18px", fontWeight: 700 }}>홍길동</p>
          <p css={{ fontSize: "14px", fontWeight: 500 }}>gildong@example.com</p>
        </div>
        <div>
          <Button onClick={() => toggleIsEdit()} variant="primary-outlined">
            <MdOutlineEdit css={{ fontSize: "18px" }} />
            저장하기
          </Button>
        </div>
      </Flex>
      <Hr />
      <Flex
        column
        css={{
          marginTop: "18px",
          marginBottom: "6px",
          "& > *+*": {
            marginTop: "18px",
          },
          label: { fontSize: "14px", fontWeight: 500 },
          "input, textarea": {
            marginTop: "4px",
            width: "100%",
          },
        }}
      >
        <Flex
          css={{
            "& > *+*": {
              marginLeft: "12px",
            },
          }}
        >
          <Button variant="white">프로필 사진 변경</Button>
        </Flex>
        <div
          css={{
            "& > *+*": {
              marginTop: "12px",
            },
          }}
        >
          <div>
            <label>자기소개</label>
            <TextArea minRows={5} maxRows={5}></TextArea>
          </div>
          <div>
            <label>닉네임</label>
            <Input placeholder="닉네임"></Input>
          </div>
          <div>
            <label>이메일</label>
            <Input placeholder="이메일"></Input>
          </div>
        </div>
      </Flex>
    </ChildrenContainer>
  );
};

const Info = () => {
  const [isEdit, toggleIsEdit] = useToggle();
  const auth = useAuth();

  if (!auth.success) return null;

  return (
    <Box
      css={{
        width: 0,
        flex: "1 0 400px",
      }}
      responsive
      column
    >
      {isEdit ? (
        <InfoEdit toggleIsEdit={toggleIsEdit} />
      ) : (
        <>
          <Flex
            css={{
              alignItems: "center",
              padding: "6px",
              marginBottom: "12px",
            }}
          >
            <div
              css={{
                overflow: "hidden",
                borderRadius: "9999px",
                height: "72px",
                width: "72px",
              }}
            >
              <Image
                src="https://i.imgur.com/tvzwhsF.png"
                alt="me"
                width="96px"
                height="96px"
              />
            </div>
            <div css={{ marginLeft: "16px", flex: "1" }}>
              <p css={{ fontSize: "18px", fontWeight: 700 }}>{auth.username}</p>
              <p css={{ fontSize: "14px", fontWeight: 500 }}>
                gildong@example.com
              </p>
            </div>
            <div>
              <Button onClick={() => toggleIsEdit()} variant="primary-outlined">
                <MdOutlineEdit css={{ fontSize: "18px" }} />
                수정하기
              </Button>
            </div>
          </Flex>
          <Hr />
          <Flex
            column
            css={{
              marginTop: "24px",
              marginBottom: "12px",
              "& > *+*": {
                marginTop: "24px",
              },
            }}
          >
            <Flex column>
              <p css={{ fontWeight: 700, marginBottom: "4px" }}>자기소개</p>
              <p>
                주는 반짝이는 그러므로 관현악이며, 것이다. 예수는 온갖 이 따뜻한
                같지 오직 이것이다. 그들은 관현악이며, 그들은 청춘을 천하를
                품으며, 광야에서 피어나기 속에 것이다. 가진 지혜는 발휘하기
                황금시대를 그들에게 뿐이다.
              </p>
            </Flex>
            <Flex column>
              <Flex
                css={{
                  display: "grid",
                  gridTemplateColumns: "auto 1fr",
                  columnGap: "32px",
                  rowGap: "4px",
                }}
              >
                {[
                  ["닉네임", auth.username!],
                  ["소셜 로그인", "Github"],
                  ["이메일", "gildong@example.com"],
                ].map((item, index) => (
                  <DetailsRow
                    item={item[0]}
                    value={item[1]}
                    index={index + 1}
                    key={String(index)}
                  />
                ))}
              </Flex>
            </Flex>
          </Flex>
        </>
      )}
    </Box>
  );
};

const Profile: CustomNextPage = () => {
  return (
    <ChildrenContainer>
      <SectionHeader>
        <SectionHeader.Title>내 프로필</SectionHeader.Title>
        <SectionHeader.Description>
          Lorem ipsum dolor sit amet, consectetur adipiscing elit.
        </SectionHeader.Description>
      </SectionHeader>
      <SectionBody>
        <Flex
          css={{
            flexWrap: "wrap",
            justifyContent: "space-between",
            width: "100%",
            alignItems: "flex-start",
            "& > *": {
              flex: 1,
            },
            gap: "24px",
          }}
        >
          <Info />
          <Flex
            column
            css={{
              width: 0,
              flex: "1 0 400px",
              gap: "24px",
            }}
          >
            {/* <FavoritePost />
            <FavoriteLounge /> */}
          </Flex>
        </Flex>
      </SectionBody>
    </ChildrenContainer>
  );
};

Profile.authorized = true;

const FavoriteLounge = () => {
  const { data, isLoading, isError } = useQuery("FavoriteLounge", async () => {
    return (await http.get<any>("/myfav")).data;
  });

  return (
    <Box responsive column>
      <p css={{ fontWeight: 600 }}>좋아요한 글</p>
      <p>{JSON.stringify(data, null, 4)}</p>
    </Box>
  );
};

const FavoritePost = () => {
  const { data, isLoading, isError } = useQuery("favoritePosts", async () => {
    return (await http.get<any>("/mypost")).data;
  });

  return (
    <Box responsive column>
      <p css={{ fontWeight: 600 }}>좋아요한 글</p>
      <p>{JSON.stringify(data, null, 4)}</p>
    </Box>
  );
};

export default Profile;
