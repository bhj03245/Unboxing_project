import cv2
import numpy as np


def region_of_interest(img, vertices):
    mask = np.zeros_like(img)
    # channel_count = img.shape[2]
    match_mask_color = 255
    cv2.fillPoly(mask, vertices, match_mask_color)
    masked_image = cv2.bitwise_and(img, mask)
    return masked_image


def get_fitline(img, f_lines):  # 대표선 구하기
    try:
        lines = np.squeeze(f_lines)

        if len(lines.shape) != 1:
            lines = lines.reshape(lines.shape[0] * 2, 2)
            rows, cols = img.shape[:2]
            output = cv2.fitLine(lines, cv2.DIST_L2, 0, 0.01, 0.01)
            vx, vy, x, y = output[0], output[1], output[2], output[3]
            # 차선변경 에러

            x1, y1 = int(((img.shape[0] - 1) - y) / vy * vx + x), int(img.shape[0] - 1)
            x2, y2 = int(((img.shape[0] / 2 + 85) - y) / vy * vx + x), int(img.shape[0] / 2 + 85)

            result = [x1, y1, x2, y2]

            return result
    except:
        return None


def draw_fit_line(img, lines, color=[255, 0, 0], thickness=10):  # 대표선 그리기
    cv2.line(img, (lines[0], lines[1]), (lines[2], lines[3]), color, thickness)


def line_intersection(line1, line2):
    xdiff = (line1[0][0] - line1[1][0], line2[0][0] - line2[1][0])
    ydiff = (line1[0][1] - line1[1][1], line2[0][1] - line2[1][1])

    def det(a, b):
        return a[0] * b[1] - a[1] * b[0]

    div = det(xdiff, ydiff)
    if div == 0:
        raise Exception('lines do not intersect')

    d = (det(*line1), det(*line2))
    x = det(d, xdiff) / div
    y = det(d, ydiff) / div
    return [x, y]


def offset(left, mid, right):
    LANEWIDTH = 3.7
    a = mid - left
    b = right - mid
    width = right - left

    if a >= b:  # driving right off
        offset = a / width * LANEWIDTH - LANEWIDTH / 2.0
    else:  # driving left off
        offset = LANEWIDTH / 2.0 - b / width * LANEWIDTH

    return offset


def process(image):
    #     # print(image.shape)
    height = image.shape[0]
    width = image.shape[1]
    region_of_interest_vertices = [
        (0, height),
        (width / 2, height / 2),
        (width, height)
    ]
    gray_image = cv2.cvtColor(image, cv2.COLOR_RGB2GRAY)

    canny_image = cv2.Canny(gray_image, 100, 120)
    cropped_image = region_of_interest(canny_image,
                                       np.array([region_of_interest_vertices], np.int32), )

    lines = cv2.HoughLinesP(cropped_image,
                            rho=2,
                            theta=np.pi / 180,
                            threshold=50,
                            lines=np.array([]),
                            minLineLength=40,
                            maxLineGap=100)

    line_arr = np.squeeze(lines)

    # 기울기 구하기
    slope_degree = (np.arctan2(line_arr[:, 1] - line_arr[:, 3], line_arr[:, 0] - line_arr[:, 2]) * 180) / np.pi

    # 수평 기울기 제한
    line_arr = line_arr[np.abs(slope_degree) < 160]
    slope_degree = slope_degree[np.abs(slope_degree) < 160]
    # 수직 기울기 제한
    line_arr = line_arr[np.abs(slope_degree) > 95]
    slope_degree = slope_degree[np.abs(slope_degree) > 95]
    # 필터링된 직선 버리기
    L_lines, R_lines = line_arr[(slope_degree > 0), :], line_arr[(slope_degree < 0), :]
    temp = np.zeros((image.shape[0], image.shape[1], 3), dtype=np.uint8)
    L_lines, R_lines = L_lines[:, None], R_lines[:, None]

    # 대표선 만들기
    left_fit_line = get_fitline(temp, L_lines)
    print('left', left_fit_line)
    right_fit_line = get_fitline(temp, R_lines)
    print('right', right_fit_line)

    if left_fit_line != None and right_fit_line != None:
        print(right_fit_line[0] - left_fit_line[0])

    color = [255, 0, 0]

    # car detection
    if left_fit_line != None and right_fit_line != None:

        A = [left_fit_line[0], left_fit_line[1]]
        B = [left_fit_line[2], left_fit_line[3]]
        C = [right_fit_line[0], right_fit_line[1]]
        D = [right_fit_line[2], right_fit_line[3]]
        intersection = line_intersection((A, B), (C, D))

        # pts = vertices.reshape((-1, 1, 2))

        car_mask = np.zeros_like(image)
        # channel_count = img.shape[2]
        match_mask_color = 255
        cv2.fillPoly(car_mask, [np.array([(intersection[0], 50), A, C], np.int32)], match_mask_color)

        car_masked_image = cv2.bitwise_and(image, car_mask)
        # cv2.imshow('ma', car_masked_image)
        car_roi_gray = cv2.cvtColor(car_masked_image, cv2.COLOR_RGB2GRAY)
        # car_roi_color = car_masked_image
        # cv2.imshow('gra', car_roi_gray)
        cars = car_cascade.detectMultiScale(car_roi_gray, 1.4, 1, minSize=(80, 80))

        for (x, y, w, h) in cars:
            print(w, h)
            cv2.rectangle(temp, (x, y), (x + w, y + h), (0, 255, 255), 2)

        center = offset(left_fit_line[0], 180, right_fit_line[0])

        print('center', abs(center))
        if abs(center) > 1.75:
            center_x = int(640 / 2.0)
            center_y = int(360 / 2.0)

            thickness = 2

            location = (center_x - 200, center_y - 100)
            font = cv2.FONT_HERSHEY_SIMPLEX;  # hand-writing style font
            fontScale = 3.5
            cv2.putText(temp, 'Warning', location, font, fontScale, (0, 0, 255), thickness)

            color = [0, 0, 255]

    if left_fit_line != None:
        draw_fit_line(temp, left_fit_line, color)

    if right_fit_line != None:
        draw_fit_line(temp, right_fit_line, color)

    image_with_lines = cv2.addWeighted(temp, 0.8, image, 1, 0.0)

    return image_with_lines


cascade_src = 'cars.xml'
cap = cv2.VideoCapture('TestVideo2.mp4')
car_cascade = cv2.CascadeClassifier(cascade_src)

while cap.isOpened():
    ret, frame = cap.read()

    if (type(frame) == type(None)):
        break

    frame = process(frame)
    cv2.imshow('frame', frame)

    if cv2.waitKey(1) & 0xFF == ord('q'):
        break

cap.release()
cv2.destroyAllWindows()
