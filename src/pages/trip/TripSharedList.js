import React, { useEffect, useState } from "react";
import PagenationComponent from "../../components/tripList/PagenationComponent";
import useCustomMove from "../../hooks/useCustomMove";
import { getSharedTripList } from "../../api/befreeApi";

const noDrag = {
  userSelect: "none",
  WebkitUserSelect: "none",
  MozUserSelect: "-moz-none",
  msUserSelect: "none",
  KhtmlUserSelect: "none",
  userDrag: "none",
  WebkitUserDrag: "none",
  KhtmlUserDrag: "none",
  MozUserDrag: "-moz-none",
  msUserDrag: "none",
};

const TripSharedList = () => {
  const { moveToTripListDetail } = useCustomMove();

  const [tripList, setTripList] = useState([]);
  const [page, setPage] = useState(1);
  const [totalPage, setTotalPage] = useState(0);

  const fetchTripList = (page) => {
    console.log("fetchTripList 호출됨");
    getSharedTripList(page).then((data) => {
      console.log("안쪽");
      let dataResult = data.RESULT.paginatedTrips;
      setTotalPage(data.RESULT.totalPage);
      console.log(dataResult);

      const tripListTemp = dataResult.map((trip) => {
        const tbegin = new Date(trip.tbegin);
        const tend = new Date(trip.tend);
        const formattedBeginDate = `${tbegin.getFullYear()}.${
          tbegin.getMonth() + 1
        }.${tbegin.getDate()}`;
        const formattedEndDate = `${tend.getFullYear()}.${
          tend.getMonth() + 1
        }.${tend.getDate()}`;

        return {
          src:
            process.env.PUBLIC_URL +
            "/assets/imgs/trip_list_size_down_" +
            trip.tid +
            ".png",
          alt: trip.tid,
          date: formattedBeginDate + " ~ " + formattedEndDate,
          title: trip.ttitle,
          region: trip.tregion,
          style: noDrag,
          shared: "true",
        };
      });

      setTripList(tripListTemp);
    });
  };

  const numButtonClicked = (buttonNumber) => {
    setPage(buttonNumber);
    console.log("눌림", buttonNumber);
  };

  useEffect(() => {
    console.log("useEffect 실행됨");
    fetchTripList(page);
  }, [page]);

  return (
    <>
      <div className="grid place-items-center">
        <span className="font-['Pretendard-Medium'] sm:text-2xl text-base mt-24">
          공유된 여행 코스
        </span>
        <div className="w-2/3 lg:w-mywidth1200 my-[1%] border-[1px] border-neutral-500"></div>
      </div>
      <div className="grid place-items-center lg:mt-10 mt-3">
        {tripList.length == 0 || tripList == [] ? (
          <>
            <div className="flex justify-center my-4 mt-40 mb-40 font-[Pretendard-Regular]">
              아직 아무도 여행 목록을 공유하지 않았습니다
            </div>
          </>
        ) : (
          <div>
            {tripList.map((item) => (
              <div className="w-2/3 lg:w-mywidth1200" key={item.src}>
                <img
                  src={item.src}
                  alt={item.alt}
                  style={item.style}
                  className="rounded-md h-14 sm:h-full hover:cursor-pointer"
                  onClick={() =>
                    moveToTripListDetail(
                      item.alt,
                      item.title,
                      item.date,
                      item.region,
                      item.shared
                    )
                  }
                ></img>
                <div className="flex justify-between">
                  <div
                    className="sm:mt-2 mb-4 hover:cursor-pointer"
                    onClick={() =>
                      moveToTripListDetail(
                        item.alt,
                        item.title,
                        item.date,
                        item.region
                      )
                    }
                  >
                    <span className="font-[Pretendard-Light] text-sm sm:text-lg">
                      {item.date}
                    </span>
                    <div className="font-[Pretendard-Light] text-sm sm:text-lg text-gray-600">
                      {item.title}
                    </div>
                  </div>
                  <div className="flex items-center sm:mt-2 mb-4"></div>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
      <PagenationComponent
        page={page}
        totalPage={totalPage}
        numButtonClicked={numButtonClicked}
        withDays={false}
      />
    </>
  );
};

export default TripSharedList;
