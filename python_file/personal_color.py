import cv2
import numpy as np
from flask import Flask
from flask import request
from flask import jsonify

from werkzeug.serving import WSGIRequestHandler
from werkzeug.utils import secure_filename
import json
WSGIRequestHandler.protocol_versionimport = "HTTP/1.1"

app = Flask(__name__)

global users
users = []

@app.route("/getuser", methods=['GET'])
def get_name():
    nameame = request.args.get('name')

    global users
    exist = False
    ret = {}
    for usr in users:
        if usr[0] == name:
            ret["username"] = usr[0]

            exist = True
            break

    return jsonify (ret)

@app.route("/sendimg", methods=['POST'])
def get_img():
    f = request.files['file']
    #f.save(secure_filename(f.filename))
    return f.filename


def checking_personal_color(a):
    img_color = cv2.imread(a)
    resize = cv2.imread(a)
    height,width = img_color.shape[:2]
    face_cascade = cv2.CascadeClassifier(cv2.data.haarcascades + 'haarcascade_frontalface_default.xml')
    gray = cv2.cvtColor(img_color, cv2.COLOR_BGR2GRAY)
    faces = face_cascade.detectMultiScale(gray,1.3,20)
    for(x,y,w,h) in faces:
        cv2.rectangle(img_color,(x,y),(x+w,y+h),(255,0,0),1)
        cropped = img_color[y:y+h,x:x+w]
        resize = cv2.resize(cropped,dsize=(0, 0), fx=1, fy=1, interpolation=cv2.INTER_AREA)

    img_hsv = cv2.cvtColor(resize, cv2.COLOR_BGR2HLS)
    h,l,s = cv2.split(img_hsv);
    l_temp = np.array(l)
    s_temp = np.array(s)
    ls_ratio = l_temp/s_temp

    canvas2 = np.zeros(shape=resize.shape, dtype=np.uint8)
    canvas2.fill(255)
    canvas2[np.where(((h<=14)|(h>=165))&(s>=50)&(ls_ratio>0.5)&(ls_ratio<3.0))] = resize[np.where(((h<=14)|(h>=165))&(s>=50)&(ls_ratio>0.5)&(ls_ratio<3.0))]
    return specific_standard(canvas2)

def specific_standard(img):
    img1 = img
    x=450
    y=350
    [r,g,b]=img1[y,x]
    m =(r/255+b/255)
    rr =r/m
    gg=g/m
    bb=b/m
    w=rr-gg
    k=rr-128
    kk=255/(2*k)
    ww=w*kk
    if r>=g>=b:
        m =(r/255+b/255)
        rr =r/m
        gg=g/m
        bb=b/m
        w=rr-gg
        k=rr-128
        kk=255/(2*k)
        ww=w*kk
        if 340>ww>=0:
            return 'youare warm tone'
        elif 1020>=ww>=340:
            return 'you are cool tone'
    elif g>=r>=b:
        m=(g/255+b/255)/255
        rr=r/m
        gg=g/m
        bb=b/m
        w=gg-rr
        k=gg-128
        kk=255/(2*k)
        ww=w*kk
        if 340>ww>=0:
            return 'You are warm tone'
        elif 1020>=ww>=340:
            return 'You are cool tone'
    elif g>=b>=r:
        m=(r/255+g/255)
        rr=r/m
        gg=g/m
        bb=b/m
        w=bb-rr+gg-rr
        k=gg-128
        kk=255/(2*k)
        ww=w*kk
        if 340>ww>=0:
            return 'You are warm tone'
        elif 1020>=ww>=340:
            return 'You are cool tone'
    elif b>=g>=r:
        m=(r/255+b/255)
        rr=r/m
        gg=g/m
        bb=b/m
        w=-(bb-gg)+2*(bb-rr)
        k=bb-128
        kk=255/(2*k)
        ww=w*kk
        if 340>ww>=0:
            return 'You are warm tone'
        elif 1020>=ww>=340:
            return 'You are cool tone'
    elif b>=r>=g:
        m=(g/255+b/255)
        rr=r/m
        gg=g/m
        bb=b/m
        w=-(bb-rr)+2*(bb-gg)
        k=bb-128
        kk=255/(2*k)
        ww=w*kk
        if 340>ww>=0:
            return 'You are warm tone'
        elif 1020>=ww>=340:
            return 'You are cool tone'
    elif r>=b>=g:
        m=(r/255+g/255)
        rr=r/m
        gg=g/m
        bb=b/m
        w=bb-gg+rr-gg
        k=rr-128
        kk=255/(2*k)
        ww=w*kk
        if 340>ww>=0:
            return 'You are warm tone'
        elif 1020>=ww>=340:
            return 'You are cool tone'


if __name__ == "__main__":
    app.run(host='127.0.0.1', port=4903,debug=True)
