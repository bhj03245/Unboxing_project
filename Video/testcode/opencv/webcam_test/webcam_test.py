import cv2

cap=cv2.VideoCapture(1) # 내장 카메라를 이용하여 외부 카메라에서 영상 받아옴

fourcc = cv2.VideoWriter_fourcc(*'DIVX') # 데이터 포맷을 지정하는 연속된 4개의 문자(코덱)
out = cv2.VideoWriter('save.mp4', fourcc, 25.0, (640,480)) # save.mp4파일로 저장

while True:
    ret, frame = cap.read() # 카메라 상태 및 프레임 받아옴
    if(ret):
        cv2.imshow('frame_color', frame) # 윈도우 창에 이미지 띄움
        out.write(frame)

        if cv2.waitKey(1) == ord('q'): # q 입력시 촬영 중지
            break

cap.release() # 카메라 장치에서 받아온 메모리 해제
cv2.destroyAllWindows() # 모든 윈도우 창 닫음