import cv2

startFrame = 250
endFrame = 500
iCurrentFrame = 0
in_file = 'infile'
out_file = 'outfile'

vc = cv2.VideoCapture(in_file)

if vc.isOpened() == False:
    print('Fail to open the video')
    exit()

fps = vc.get(cv2.CAP_PROP_FPS)
width = int(vc.get(cv2.CAP_PROP_FRAME_WIDTH))
height = int(vc.get(cv2.CAP_PROP_FRAME_HEIGHT))
fourcc = cv2.VideoWriter_fourcc(*'XVID')

vw = cv2.VideoWriter(out_file, fourcc, fps, (width, height))

if ((startFrame < 0 or startFrame >= vc.get(cv2.CAP_PROP_FRAME_COUNT)) or
        (endFrame < 0 or endFrame >= vc.get(cv2.CAP_PROP_FRAME_COUNT))):
    print("Wrong Frame")
    exit()

vc.set(cv2.CAP_PROP_POS_FRAMES, startFrame)
while True:
    ret, frame = vc.read()
    if (iCurrentFrame > (endFrame - startFrame)):
        break
    iCurrentFrame += 1

    vw.write(frame)
    cv2.imshow("image", frame)
    if cv2.waitKey(1) & 0xFF == 27:
        break

vc.release()
vw.release()


