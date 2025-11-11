; ===========================================
; Ctrl + Shift + V = 개행·탭 제거 후 붙여넣기 (v2 문법)
; ===========================================

^+v:: { ; Ctrl(^) + Shift(+) + V
    try {
        text := A_Clipboard
        ; 모든 줄바꿈(\r, \n)을 공백으로 변환
        text := RegExReplace(text, "\R+", " ")
        ; 연속된 공백/탭을 하나로
        text := RegExReplace(text, "[ \t]{2,}", " ")
        ; SQL용 작은따옴표 이스케이프 (' → '')
        text := StrReplace(text, "'", "''")

        A_Clipboard := text
        Send("^v")   ; 정리된 내용 붙여넣기
    } catch as e {
        MsgBox("에러 발생: " . e.Message)
    }
}