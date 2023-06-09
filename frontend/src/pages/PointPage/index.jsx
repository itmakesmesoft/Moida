import { useState, useEffect } from "react";
import styled from "styled-components";
import tw from "twin.macro";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import { useWeb3React } from "@web3-react/core";

// 수연: 포인트 충전 페이지
const PointPage = () => {
  const [currentPoint, setCurrentPoint] = useState(0);
  const navigate = useNavigate();
  const donate = async (price) => {
    setCurrentPoint(currentPoint + price);
  };

  const [kakaoUrl, SetKakaoUrl] = useState("");
  useEffect(() => {
    if (currentPoint !== 0) {
      axios({
        url: "https://kapi.kakao.com/v1/payment/ready",
        method: "POST",
        headers: {
          Authorization: "KakaoAK 75072266177df82ab4bc1574f658a897",
          "Content-type": "application/x-www-form-urlencoded;charset=utf-8",
        },
        next_redirect_pc_url: "",
        tid: "",
        params: {
          cid: "TC0ONETIME",
          partner_order_id: "partner_order_id",
          partner_user_id: "partner_user_id",
          item_name: "모이다 기부포인트",
          quantity: 1,
          total_amount: currentPoint,
          tax_free_amount: 0,
          vat_amount: 200,
          approval_url: "http://j8c2071.p.ssafy.io/payresult",
          fail_url: "http://j8c2071.p.ssafy.io/payresult",
          cancel_url: "http://j8c2071.p.ssafy.io/payresult",
        },
      })
        .then((res) => {
          // console.log(res.data.next_redirect_pc_url);
          window.localStorage.setItem("tid", res.data.tid);
          SetKakaoUrl(res.data.next_redirect_pc_url);
        })
        .catch((error) => {
          console.log(error);
        });
    }
  }, [currentPoint]);

  const {
    account, // DApp에 연결된 account address
    connector,
    activate,
  } = useWeb3React();

  const kakaoPay = () => {
    if (kakaoUrl === "") {
      alert("1000원 이상 결제만 가능합니다.");
    } else if (!(account && connector)) {
      alert(
        "메타마스크가 연결되어있지 않습니다. 메타마스크 연결 페이지로 이동합니다."
      );
      navigate("/check", { replace: false });
    } else {
      window.open(kakaoUrl);
      // 자식창에서 버튼 눌렀을 때 동작
      window.parentCallback = (page) => {
        try {
          window.open("", "_self").close();
        } catch {
          console.log("");
        }
      };
    }
  };
  return (
    <Container>
      <PointContainer>
        <RightSide>
          <PointForm>
            <Title>충전할 금액 설정</Title>
            <InnerBox>
              <Text>충전 금액</Text>
              <PointText>{currentPoint.toLocaleString("ko-KR")} 원</PointText>
            </InnerBox>
            <InnerBox>
              <Text>VAT (10%)</Text>
              <PointText>
                {parseInt(currentPoint / 10).toLocaleString("ko-KR")} 원
              </PointText>
            </InnerBox>
            <GroupButton>
              <PointButton
                onClick={(e) => {
                  e.preventDefault();
                  setCurrentPoint(0);
                  SetKakaoUrl("");
                }}
              >
                <Text>초기화</Text>
              </PointButton>
              <PointButton
                onClick={(e) => {
                  e.preventDefault();
                  donate(50000);
                }}
              >
                <Text>+50,000</Text>
              </PointButton>

              <PointButton
                onClick={(e) => {
                  e.preventDefault();
                  donate(10000);
                }}
              >
                <Text>+10,000</Text>
              </PointButton>

              <PointButton
                onClick={(e) => {
                  e.preventDefault();
                  donate(5000);
                }}
              >
                <Text>+5,000</Text>
              </PointButton>

              <PointButton
                onClick={(e) => {
                  e.preventDefault();
                  donate(1000);
                }}
              >
                <Text>+1,000</Text>
              </PointButton>
            </GroupButton>
            <Line></Line>
            <Title>최종 결제 금액</Title>
            <Box>
              <KakaoBox>
                <Text>간편결제</Text>
                <PointText>카카오페이</PointText>
              </KakaoBox>
              <div>
                <PayText>
                  {parseInt(currentPoint * 1.1).toLocaleString("ko-KR")} 원
                </PayText>
                <PayTextBottom>(VAT 포함)</PayTextBottom>
              </div>
            </Box>
            <SubmitButton
              type="submit"
              onClick={(e) => {
                e.preventDefault();
                kakaoPay();
              }}
            >
              결제하기
            </SubmitButton>
          </PointForm>
        </RightSide>
      </PointContainer>
    </Container>
  );
};

const Container = styled.div`
  height: calc(100vh - 100px);
  width: 100vw;
  background-color: rgb(250, 250, 243);
  display: flex;
  -webkit-box-align: center;
  -webkit-box-pack: justify;
  justify-content: space-between;
  align-items: flex-start;
  padding-bottom: 100px;
`;

const PointContainer = styled.div`
  ${tw` 
  mx-auto bg-white h-auto w-auto shadow-lg shadow-gray-400 rounded-md
  `};
`;

const Title = styled.h2`
  ${tw`
  text-left	text-xl font-black text-gray-900 mb-5 tracking-tight
  `}
`;

const RightSide = styled.div`
  ${tw`
  px-10
`}
`;

const InnerBox = styled.div`
  ${tw`
flex items-center justify-between
`}
`;

const Box = styled.div`
  background-color: rgb(244, 248, 242);
  ${tw` 
  grid grid-cols-2 h-24 rounded-md
  flex items-center justify-between 
`}
`;

const KakaoBox = styled.div`
  ${tw`
  text-left pl-5 
  `}
`;

const PointForm = styled.form`
  ${tw`
  mt-10
`}
`;

const GroupButton = styled.div`
  ${tw`
  grid grid-cols-5 gap-1 mt-5 mb-2
  `}
`;

const PointButton = styled.button`
  ${tw`
  border border-gray-400 rounded-xl px-3 mx-1
  `}
`;

const Text = styled.h3`
  color: rgb(98, 98, 98);
  ${tw`
  text-sm font-normal my-2
  `}
`;

const PointText = styled.h3`
  ${tw`
  text-sm text-gray-900 font-black	my-2

  `}
`;

const PayText = styled.h3`
  color: rgb(160, 200, 70);
  ${tw`
  text-sm font-black	my-2 text-right mr-5
  `}
`;
const PayTextBottom = styled.h3`
  color: rgb(98, 98, 98);
  ${tw`
  text-sm font-normal my-2 text-right mr-5
  `}
`;

const SubmitButton = styled.button`
  background-color: rgb(160, 200, 70);
  ${tw`
  w-full h-full mt-10 py-4 px-10 font-normal text-white rounded-lg  mb-7
  `}
`;
const Line = styled.hr`
  ${tw`
  my-10
  `}
`;

export default PointPage;
