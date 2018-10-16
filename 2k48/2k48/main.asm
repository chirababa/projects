.386
.model flat, stdcall
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

includelib msvcrt.lib
extern exit: proc
extern malloc: proc
extern memset: proc
extern printf: proc

includelib canvas.lib
extern BeginDrawing: proc


public start

.data
include digits.inc
include letters.inc
include grid.inc
;aici declaram date
window_title DB "2k48",0
area_width EQU 640
area_height EQU 480
area DD 0

counter DD 0 ; numara evenimentele de tip timer
counter1 dd 0

ajutor1 dd 4
ajutor2 dd 4

arg1 EQU 8
arg2 EQU 12
arg3 EQU 16
arg4 EQU 20
format db "%d", 13, 10, "%d", 13, 10, 0; format afisam pozitia unde am dat clik
format1 db "%d",13, 10, 0

pix dd 4
pozx dd 0; retinem pozitia x a clickului
pozy dd 0; retinem pozitia y a clickului

matrice dd 80, 80, 80, 80
	    dd 80, 80, 65, 80
		dd 80, 80, 80, 80
		dd 65, 65, 65, 80
	 
symbol_width EQU 44
symbol_height EQU 60

.code
; procedura make_text afiseaza o litera sau o cifra la coordonatele date
; arg1 - simbolul de afisat (litera sau cifra)
; arg2 - pointer la vectorul de pixeli
; arg3 - pos_x
; arg4 - pos_y
make_text proc
	push ebp
	mov ebp, esp
	pusha
	
	mov eax, [ebp+arg1] ; citim simbolul de afisat
	cmp eax, 'A'
	jl make_space
	cmp eax, 'Q'
	jg make_space
	sub eax, 'A'
	lea esi, grid
	jmp draw_text
make_space:	
	mov eax, 26 ; de la 0 pana la 25 sunt litere, 26 e space
	lea esi, letters
	
draw_text:
	mov ebx, symbol_width
	mul ebx
	mov ebx, symbol_height
	mul ebx
	add esi, eax
	mov ecx, symbol_height
bucla_simbol_linii:
	mov edi, [ebp+arg2] ; pointer la matricea de pixeli
	mov eax, [ebp+arg4] ; pointer la coord y
	add eax, symbol_height
	sub eax, ecx
	mov ebx, area_width
	mul ebx
	add eax, [ebp+arg3] ; pointer la coord x
	shl eax, 2 ; inmultim cu 4, avem un DWORD per pixel
	add edi, eax
	push ecx
	mov ecx, symbol_width
bucla_simbol_coloane:
	cmp byte ptr [esi], 0
	je simbol_pixel_alb
	mov dword ptr [edi], 0
	jmp simbol_pixel_next
simbol_pixel_alb:
	mov dword ptr [edi], 0FFFFFFh
simbol_pixel_next:
	inc esi
	add edi, 4
	loop bucla_simbol_coloane
	pop ecx
	loop bucla_simbol_linii
	popa
	mov esp, ebp
	pop ebp
	ret
make_text endp

make_text_macro macro symbol, drawArea, x, y
	push y
	push x
	push drawArea
	push symbol
	call make_text
	add esp, 16
endm

; functia de desenare - se apeleaza la fiecare click
; sau la fiecare interval de 200ms in care nu s-a dat click
; arg1 - evt (0 - initializare, 1 - click, 2 - s-a scurs intervalul fara click)
; arg2 - x
; arg3 - y
draw proc
	push ebp
	mov ebp, esp
	pusha
	
	mov eax, [ebp+arg1]
	cmp eax, 1
	jz evt_click
	cmp eax, 2
	jz evt_timer ; nu s-a efectuat click pe nimic
	;mai jos e codul care intializeaza fereastra cu pixeli albi
	mov eax, area_width
	mov ebx, area_height
	mul ebx
	shl eax, 2
	push eax
	push 255
	push area
	call memset
	add esp, 12
	jmp afisare_litere
	
evt_click:
	mov edi, area
	mov ecx, area_height
	mov ebx, [ebp+arg3]
	and ebx, 7
	inc ebx
	
	mov eax, [ebp+arg2]
	mov pozx, eax
	mov eax, [ebp+arg3]
	mov pozy, eax
	
	push pozy
	push pozx
	push offset format
	call printf
	add esp, 12
	
	call apasare_butoane
	
	
evt_timer:
	inc counter
	inc counter1
	
afisare_litere:
	mov eax,0
	mov esi,0
	mov edi,0
	;scriem un mesaj
	make_text_macro matrice[edi][esi], area, 110, 100 
	add esi,4
	make_text_macro matrice[edi][esi], area, 154, 100 
	add esi,4
	make_text_macro matrice[edi][esi], area, 198, 100
	add esi,4
	make_text_macro matrice[edi][esi], area, 242, 100 
	
	add edi,16
	mov esi,0
	make_text_macro matrice[edi][esi], area, 110, 160 
	add esi,4
	make_text_macro matrice[edi][esi], area, 154, 160 
	add esi,4
	make_text_macro matrice[edi][esi], area, 198, 160 
	add esi,4
	make_text_macro matrice[edi][esi], area, 242, 160 
	
	add edi,16
	mov esi,0
	make_text_macro matrice[edi][esi], area, 110, 220 
	add esi,4
	make_text_macro matrice[edi][esi], area, 154, 220 
	add esi,4
	make_text_macro matrice[edi][esi], area, 198, 220 
	add esi,4
	make_text_macro matrice[edi][esi], area, 242, 220 
	
	add edi,16
	mov esi,0
	make_text_macro matrice[edi][esi], area, 110, 280 
	add esi,4
	make_text_macro matrice[edi][esi], area, 154, 280
	add esi,4
	make_text_macro matrice[edi][esi], area, 198, 280 
	add esi,4
	make_text_macro matrice[edi][esi], area, 242, 280 
	
	make_text_macro 'L', area, 420, 130 ; buton sus
	make_text_macro 'M', area, 420, 234 ; buton jos
	make_text_macro 'N', area, 490, 180 ; buton dreapta
	make_text_macro 'O', area, 350, 180 ; buton stanga
final_draw:
	popa
	mov esp, ebp
	pop ebp
	ret
draw endp

; Procedura de verificare apasarea butoanelor 
apasare_butoane proc
		
apasare_buton_sus:	
	cmp pozx,420
	jl apasare_buton_stanga
	cmp pozx,464
	jg apasare_buton_dreapta
	cmp pozy,130
	jl return
	cmp pozy,189
	jg apasare_buton_jos
	
	jmp miscare_sus

apasare_buton_stanga:
	cmp pozx,350
	jl return
	cmp pozx,394
	jg return
	cmp pozy,180
	jl return
	cmp pozy,239
	jg return
	
	jmp miscare_stanga

apasare_buton_dreapta:
	cmp pozx,490
	jl return
	cmp pozx,533
	jg return
	cmp pozy,180
	jl return
	cmp pozy,239
	jg return
	jmp miscare_dreapta
	
apasare_buton_jos:
	cmp pozx,420
	jl return
	cmp pozx,463
	jg return
	cmp pozy,234
	jl return
	cmp pozy,293
	jg return
	jmp miscare_jos
	
;initializare 
miscare_sus:
	mov edi,0
	mov esi,0
	mov pix,4
	jmp eticheta
	
eticheta:;parcurgere primele 2 linii 
	dec pix
	mov eax,matrice[edi][esi]
	mov ebx,matrice[edi+16][esi]
	
	mov ecx,matrice[edi+32][esi];;;;;; 4 2 0 2
	
	cmp eax,75
	je mesaj
	
	cmp eax,80
	je dublare_spatiu
	
	cmp ebx,80
	je caz_special
	
	cmp ecx,80
	je caz_special_3
	
	cmp eax,ebx
	je dublare
	
	add esi,4
	cmp pix,0
	jne eticheta
	jmp miscare_sus_1
	
	
caz_special:;4 0 2 2
	mov ecx,matrice[edi+32][esi]
	cmp ecx,80
	je caz_special_1
	
	mov matrice[edi+16][esi], ecx
	mov ecx,matrice[edi+48][esi]
	mov matrice[edi+32][esi],ecx
	mov matrice[edi+48][esi],80
	
	mov eax,matrice[edi][esi]
	mov ebx,matrice[edi+16][esi]
	cmp eax,ebx
	je dublare

	jmp eroare_spatiu

caz_special_1:;2 0 0 2
	mov ecx,matrice[edi+48][esi]
	mov matrice[edi+16][esi],ecx
	mov matrice[edi+48][esi],80
	mov eax,matrice[edi][esi]
	mov ebx,matrice[edi+16][esi]
	cmp eax,ebx
	je dublare
	
	jmp eroare_spatiu

caz_special_2:;0 2 0 2
	mov ecx,matrice[edi+16][esi]
	mov matrice[edi+16][esi],80
	mov matrice[edi][esi],ecx
	mov edx,matrice[edi+48][esi]
	mov matrice[edi+48][esi],80
	mov matrice[edi+16][esi],edx
	mov eax,matrice[edi][esi]
	mov ebx,matrice[edi+16][esi]
	cmp eax,ebx
	je dublare
	jmp eroare_spatiu

caz_special_3:;4 2 0 2
	mov ecx,matrice[edi+48][esi]
	mov matrice[edi+32][esi], ecx
	mov matrice[edi+48][esi],80
	mov eax,matrice[edi][esi]
	mov ebx,matrice[edi+16][esi]
	cmp eax,ebx
	je dublare
	jmp eroare_spatiu
	
;daca avem match pe primele 2 linii se face adunarea 
dublare:
	cmp eax,80
	je eroare_spatiu
    add matrice[edi][esi],1
	mov eax,matrice[edi+32][esi]
	mov ebx,matrice[edi+48][esi]
	mov matrice[edi+16][esi],eax
	mov matrice[edi+32][esi],ebx
	mov matrice[edi+48][esi],80
	add esi,4
	cmp pix,0
	jne eticheta
	jmp miscare_sus_1
	
;cazul in care avem pe prima linie spatiu 
dublare_spatiu:
	cmp ebx,80
	je mutare_sus_2_spatii
	
	mov edx,matrice[edi+32][esi]
	cmp edx,80
	je caz_special_2
	
	mov ecx,matrice[edi+16][esi]
	mov matrice[edi][esi],ecx
	mov edx,matrice[edi+32][esi]
	mov matrice[edi+16][esi],edx
	mov ecx,matrice[edi+48][esi]
	mov matrice[edi+32][esi],ecx
	mov matrice[edi+48][esi],80
	
	
	mov eax,matrice[edi][esi]
	mov ebx,matrice[edi+16][esi]
	cmp eax,ebx
	je dublare
	jmp eroare_spatiu


;cazul in care avem spatiu pe prima si a 2-a 
mutare_sus_2_spatii:
	mov ecx,matrice[edi+32][esi]
	cmp ecx,80
	je mutare_sus_3_spatii
	mov matrice[edi][esi],ecx
	mov edx,matrice[edi+48][esi]
	mov matrice[edi+16][esi],edx
	mov matrice[edi+32][esi],80
	mov matrice[edi+48][esi],80
	
	mov eax,matrice[edi][esi]
	mov ebx,matrice[edi+16][esi]
	
	cmp eax,ebx
	je dublare
	jmp eroare_spatiu
	
;cazul in care avem pe primele 3 linii spatiu 
mutare_sus_3_spatii:
	mov ecx,matrice[edi+48][esi]
	mov matrice[edi][esi],ecx
	mov matrice[edi+16][esi],80
	mov matrice[edi+32][esi],80
	mov matrice[edi+48][esi],80
	jmp eroare_spatiu

;incrementare coloana 
eroare_spatiu:
	add esi,4
	cmp pix,0
	je miscare_sus_1
	jmp eticheta

	
miscare_sus_1:
	mov edi,16
	mov esi,0
	mov pix,4
	jmp eticheta_1

eticheta_1:
	
	
	dec pix
	mov eax,matrice[edi][esi]
	mov ebx,matrice[edi+16][esi]
	
	cmp eax,75
	je mesaj
	
	cmp eax,80
	je dublare_spatiu_1
	
	cmp eax,ebx
	je dublare_1
	
	add esi,4
	cmp pix,0
	jne eticheta_1
	jmp miscare_sus_2
	
dublare_1:
	cmp eax,80
	je eroare_spatiu_1
    add matrice[edi][esi],1
	mov ebx,matrice[edi+32][esi]
	mov matrice[edi+16][esi],ebx
	mov matrice[edi+32][esi],80
	mov eax,matrice[edi][esi]
	mov ebx,matrice[edi+16][esi]
	add esi,4
	cmp pix,0
	je miscare_sus_2
	jmp eticheta_1

dublare_spatiu_1:
	cmp ebx,80
	je mutare_sus_2_spatii_1
	mov ecx,matrice[edi+16][esi]
	mov matrice[edi][esi],ecx
	mov edx,matrice[edi+32][esi]
	mov matrice[edi+16][esi],edx
	mov matrice[edi+32][esi],80
	
	mov eax,matrice[edi][esi]
	mov ebx,matrice[edi+16][esi]
	
	cmp eax,ebx
	je dublare_1
	jmp eroare_spatiu_1
	
mutare_sus_2_spatii_1:
	mov eax,matrice[edi+32][esi]
	mov matrice[edi][esi],eax
	mov matrice[edi+32][esi],80
	jmp eroare_spatiu_1

eroare_spatiu_1:
	add esi,4
	cmp pix,0
	je miscare_sus_2
	jmp eticheta_1
	
	
miscare_sus_2:
	mov edi,32
	mov esi,0
	mov pix,4
	jmp eticheta_2
	
eticheta_2:
	cmp eax,75
	je mesaj
	
	dec pix
	mov eax,matrice[edi][esi]
	mov ebx,matrice[edi+16][esi]
	cmp eax,80
	je dublare_spatiu_2
	cmp eax,ebx
	je dublare_2
	
	add esi,4
	cmp pix,0
	jne eticheta_2
	jmp final_apasare_butoane
	
dublare_2:
	cmp eax,80
	je eroare_spatiu_2
    add matrice[edi][esi],1
	mov matrice[edi+16][esi],80
	add esi,4
	cmp pix,0
	je final_apasare_butoane
	jmp eticheta_2

dublare_spatiu_2:
	mov ecx,matrice[edi+16][esi]
	mov matrice[edi][esi],ecx
	mov matrice[edi+16][esi],80
	jmp eroare_spatiu_2
	
eroare_spatiu_2:
	add esi,4
	cmp pix,0
	je final_apasare_butoane
	jmp eticheta_2

	
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;initializare
miscare_stanga:
	mov edi,0
	mov esi,0
	mov pix,4
	jmp eticheta_stanga

;parcurgere primele 2 linii 
	
eticheta_stanga:
	dec pix
	mov eax,matrice[edi][esi]
	mov ebx,matrice[edi][esi+4]
	mov ecx,matrice[edi][esi+8]
	
	cmp eax,75
	je mesaj
	
	cmp eax,80
	je dublare_spatiu_stanga
	
	cmp ebx,80
	je caz_special_stanga

	cmp ecx,80
	je caz_special_stanga_3
	
	cmp eax,ebx
	je dublare_stanga
	
	add edi,16
	cmp pix,0
	jne eticheta_stanga
	jmp miscare_stanga_1
	
;4 0 2 2	
caz_special_stanga:
	mov ecx,matrice[edi][esi+8]
	cmp ecx,80
	je caz_special_1_stanga
	
	mov matrice[edi][esi+4], ecx
	mov ecx,matrice[edi][esi+12]
	mov matrice[edi][esi+8],ecx
	mov matrice[edi][esi+12],80
	
	mov eax,matrice[edi][esi]
	mov ebx,matrice[edi][esi+4]
	cmp eax,ebx
	je dublare_stanga

	jmp eroare_spatiu_stanga
	
;2 0 0 2
caz_special_1_stanga:
	mov ecx,matrice[edi][esi+12]
	mov matrice[edi][esi+4],ecx
	mov matrice[edi][esi+12],80
	mov eax,matrice[edi][esi]
	mov ebx,matrice[edi][esi+4]
	cmp eax,ebx
	je dublare_stanga
	jmp eroare_spatiu_stanga

;0 2 0 2
caz_special_2_stanga:
	mov ecx,matrice[edi][esi+4]
	mov matrice[edi][esi+4],80
	mov matrice[edi][esi],ecx
	mov edx,matrice[edi][esi+12]
	mov matrice[edi][esi+12],80
	mov matrice[edi][esi+4],edx
	mov eax,matrice[edi][esi]
	mov ebx,matrice[edi][esi+4]
	cmp eax,ebx
	je dublare_stanga
	jmp eroare_spatiu_stanga

caz_special_stanga_3:;4 2 0 2
	mov ecx,matrice[edi][esi+12]
	mov matrice[edi][esi+8], ecx
	mov matrice[edi][esi+12],80
	
	mov eax,matrice[edi][esi]
	mov ebx,matrice[edi][esi+4]
	cmp eax,ebx
	je dublare_stanga
	jmp eroare_spatiu_stanga

;daca avem match pe primele 2 coloane facem adunarea
dublare_stanga:
	cmp eax,80
	je eroare_spatiu_stanga
    add matrice[edi][esi],1
	mov eax,matrice[edi][esi+8]
	mov ebx,matrice[edi][esi+12]
	mov matrice[edi][esi+4],eax
	mov matrice[edi][esi+8],ebx
	mov matrice[edi][esi+12],80
	add edi,16
	cmp pix,0
	jne eticheta_stanga
	jmp miscare_stanga_1
	
; cazul in care avem pe prima  coloana un spatiu
dublare_spatiu_stanga:
	cmp ebx,80
	je mutare_stanga_2_spatii
	
	mov edx,matrice[edi][esi+8]
	cmp edx,80
	je caz_special_2_stanga
	
	mov ecx,matrice[edi][esi+4]
	mov matrice[edi][esi],ecx
	mov edx,matrice[edi][esi+8]
	mov matrice[edi][esi+4],edx
	mov ecx,matrice[edi][esi+12]
	mov matrice[edi][esi+8],ecx
	mov matrice[edi][esi+12],80
	
	
	mov eax,matrice[edi][esi]
	mov ebx,matrice[edi][esi+4]
	cmp eax,ebx
	je dublare_stanga
	jmp eroare_spatiu_stanga


	
; avem spatiu pe prima si a 2 a coloana	
mutare_stanga_2_spatii:
	mov ecx,matrice[edi][esi+8]
	cmp ecx,80
	je mutare_sus_3_spatii_stanga
	mov matrice[edi][esi],ecx
	mov edx,matrice[edi][esi+12]
	mov matrice[edi][esi+4],edx
	mov matrice[edi][esi+8],80
	mov matrice[edi][esi+12],80
	
	mov eax,matrice[edi][esi]
	mov ebx,matrice[edi][esi+4]
	
	cmp eax,ebx
	je dublare_stanga
	jmp eroare_spatiu_stanga

;primele 3 coloane sunt spatii	
mutare_sus_3_spatii_stanga:
	mov ecx,matrice[edi][esi+12]
	mov matrice[edi][esi],ecx
	mov matrice[edi][esi+4],80
	mov matrice[edi][esi+8],80
	mov matrice[edi][esi+12],80
	jmp eroare_spatiu_stanga
	
;incrementare la linia urmatoare	
eroare_spatiu_stanga:
	add edi,16
	cmp pix,0
	je miscare_stanga_1
	jmp eticheta_stanga

	
miscare_stanga_1:
	mov edi,0
	mov esi,4
	mov pix,4
	jmp eticheta_1_stanga

eticheta_1_stanga:
	dec pix
	mov eax,matrice[edi][esi]
	mov ebx,matrice[edi][esi+4]
	
	cmp eax,75
	je mesaj
	
	cmp eax,80
	je dublare_spatiu_1_stanga
	
	cmp eax,ebx
	je dublare_1_stanga
	
	add edi,16
	cmp pix,0
	jne eticheta_1_stanga
	jmp miscare_2_stanga
	
dublare_1_stanga:
	cmp eax,80
	je eroare_spatiu_1_stanga
    add matrice[edi][esi],1
	mov ebx,matrice[edi][esi+8]
	mov matrice[edi][esi+4],ebx
	mov matrice[edi][esi+8],80
	mov eax,matrice[edi][esi]
	mov ebx,matrice[edi][esi+4]
	add edi,16
	cmp pix,0
	je miscare_2_stanga
	jmp eticheta_1_stanga

dublare_spatiu_1_stanga:
	cmp ebx,80
	je mutare_sus_2_spatii_1_stanga
	mov ecx,matrice[edi][esi+4]
	mov matrice[edi][esi],ecx
	mov edx,matrice[edi][esi+8]
	mov matrice[edi][esi+4],edx
	mov matrice[edi][esi+8],80
	
	mov eax,matrice[edi][esi]
	mov ebx,matrice[edi][esi+4]
	
	cmp eax,ebx
	je dublare_1_stanga
	jmp eroare_spatiu_1_stanga
	
mutare_sus_2_spatii_1_stanga:
	mov eax,matrice[edi][esi+8]
	mov matrice[edi][esi],eax
	mov matrice[edi][esi+8],80
	jmp eroare_spatiu_1_stanga

eroare_spatiu_1_stanga:
	add edi,16
	cmp pix,0
	je miscare_2_stanga
	jmp eticheta_1_stanga
	
miscare_2_stanga:
	mov edi,0
	mov esi,8
	mov pix,4
	jmp eticheta_2_stanga
	
eticheta_2_stanga:
	dec pix
	mov eax,matrice[edi][esi]
	mov ebx,matrice[edi][esi+4]
	
	cmp eax,75
	je mesaj
	
	cmp eax,80
	je dublare_spatiu_2_stanga
	cmp eax,ebx
	je dublare_2_stanga
	
	add edi,16
	cmp pix,0
	jne eticheta_2_stanga
	jmp final_apasare_butoane
	
dublare_2_stanga:
	cmp eax,80
	je eroare_spatiu_2_Stanga
    add matrice[edi][esi],1
	mov matrice[edi][esi+4],80
	add edi,16
	cmp pix,0
	je final_apasare_butoane
	jmp eticheta_2_stanga

dublare_spatiu_2_stanga:

	mov ecx,matrice[edi][esi+4]
	mov matrice[edi][esi],ecx
	mov matrice[edi][esi+4],80
	jmp eroare_spatiu_2_stanga
	
eroare_spatiu_2_stanga:
	add edi,16
	cmp pix,0
	je final_apasare_butoane
	jmp eticheta_2_stanga	
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;initializare 
miscare_jos:
	mov edi,48
	mov esi,0
	mov pix,4
	jmp eticheta_jos
	
eticheta_jos:;parcurgere primele 2 linii 
	
	dec pix
	mov eax,matrice[edi][esi]
	mov ebx,matrice[edi-16][esi]
	mov ecx,matrice[edi-32][esi]
	
	cmp eax,75
	je mesaj
	
	cmp eax,80
	je dublare_spatiu_jos
	
	cmp ebx,80
	je caz_special_jos
	
	cmp ecx,80
	je caz_special_jos_3
	
	cmp eax,ebx
	je dublare_jos
	
	add esi,4
	cmp pix,0
	jne eticheta_jos
	jmp miscare_jos_1
	
	
caz_special_jos:;4 0 2 2
	mov ecx,matrice[edi-32][esi]
	cmp ecx,80
	je caz_special_1_jos
	
	mov matrice[edi-16][esi], ecx
	mov ecx,matrice[edi-48][esi]
	mov matrice[edi-32][esi],ecx
	mov matrice[edi-48][esi],80
	
	mov eax,matrice[edi][esi]
	mov ebx,matrice[edi-16][esi]
	cmp eax,ebx
	je dublare_jos

	jmp eroare_spatiu_jos

caz_special_1_jos:;2 0 0 2
	mov ecx,matrice[edi-48][esi]
	mov matrice[edi-16][esi],ecx
	mov matrice[edi-48][esi],80
	mov eax,matrice[edi][esi]
	mov ebx,matrice[edi-16][esi]
	cmp eax,ebx
	je dublare_jos
	
	jmp eroare_spatiu_jos

caz_special_2_jos:;0 2 0 2
	mov ecx,matrice[edi-16][esi]
	mov matrice[edi-16][esi],80
	mov matrice[edi][esi],ecx
	mov edx,matrice[edi-48][esi]
	mov matrice[edi-48][esi],80
	mov matrice[edi-16][esi],edx
	mov eax,matrice[edi][esi]
	mov ebx,matrice[edi-16][esi]
	cmp eax,ebx
	je dublare_jos
	jmp eroare_spatiu_jos

caz_special_jos_3:
	mov ecx,matrice[edi-48][esi]
	mov matrice[edi-32][esi], ecx
	mov matrice[edi-48][esi],80
	mov eax,matrice[edi][esi]
	mov ebx,matrice[edi-16][esi]
	cmp eax,ebx
	je dublare_jos
	jmp eroare_spatiu_jos

;daca avem match pe primele 2 linii se face adunarea 
dublare_jos:
	cmp eax,80
	je eroare_spatiu_jos
    add matrice[edi][esi],1
	mov eax,matrice[edi-32][esi]
	mov ebx,matrice[edi-48][esi]
	mov matrice[edi-16][esi],eax
	mov matrice[edi-32][esi],ebx
	mov matrice[edi-48][esi],80
	add esi,4
	cmp pix,0
	jne eticheta_jos
	jmp miscare_jos_1
	
;cazul in care avem pe prima linie spatiu 
dublare_spatiu_jos:
	cmp ebx,80
	je mutare_jos_2_spatii
	
	mov edx,matrice[edi-32][esi]
	cmp edx,80
	je caz_special_2_jos
	
	mov ecx,matrice[edi-16][esi]
	mov matrice[edi][esi],ecx
	mov edx,matrice[edi-32][esi]
	mov matrice[edi-16][esi],edx
	mov ecx,matrice[edi-48][esi]
	mov matrice[edi-32][esi],ecx
	mov matrice[edi-48][esi],80
	
	
	mov eax,matrice[edi][esi]
	mov ebx,matrice[edi-16][esi]
	cmp eax,ebx
	je dublare_jos
	jmp eroare_spatiu_jos


;cazul in care avem spatiu pe prima si a 2-a 
mutare_jos_2_spatii:
	mov ecx,matrice[edi-32][esi]
	cmp ecx,80
	je mutare_jos_3_spatii
	mov matrice[edi][esi],ecx
	mov edx,matrice[edi-48][esi]
	mov matrice[edi-16][esi],edx
	mov matrice[edi-32][esi],80
	mov matrice[edi-48][esi],80
	
	mov eax,matrice[edi][esi]
	mov ebx,matrice[edi-16][esi]
	
	cmp eax,ebx
	je dublare_jos
	jmp eroare_spatiu_jos
	
;cazul in care avem pe primele 3 linii spatiu 
mutare_jos_3_spatii:
	mov ecx,matrice[edi-48][esi]
	mov matrice[edi][esi],ecx
	mov matrice[edi-16][esi],80
	mov matrice[edi-32][esi],80
	mov matrice[edi-48][esi],80
	jmp eroare_spatiu_jos

;incrementare coloana 
eroare_spatiu_jos:
	add esi,4
	cmp pix,0
	je miscare_jos_1
	jmp eticheta_jos

	
miscare_jos_1:
	mov edi,32
	mov esi,0
	mov pix,4
	jmp eticheta_1_jos

eticheta_1_jos:
	cmp eax,75
	je mesaj
	dec pix
	mov eax,matrice[edi][esi]
	mov ebx,matrice[edi-16][esi]
	cmp eax,80
	je dublare_spatiu_1_jos
	
	cmp eax,ebx
	je dublare_1_jos
	
	add esi,4
	cmp pix,0
	jne eticheta_1_jos
	jmp miscare_jos_2
	
dublare_1_jos:
	cmp eax,80
	je eroare_spatiu_1_jos
    add matrice[edi][esi],1
	mov ebx,matrice[edi-32][esi]
	mov matrice[edi-16][esi],ebx
	mov matrice[edi-32][esi],80
	mov eax,matrice[edi][esi]
	mov ebx,matrice[edi-16][esi]
	add esi,4
	cmp pix,0
	je miscare_jos_2
	jmp eticheta_1_jos

dublare_spatiu_1_jos:
	cmp ebx,80
	je mutare_jos_2_spatii_1
	mov ecx,matrice[edi-16][esi]
	mov matrice[edi][esi],ecx
	mov edx,matrice[edi-32][esi]
	mov matrice[edi-16][esi],edx
	mov matrice[edi-32][esi],80
	
	mov eax,matrice[edi][esi]
	mov ebx,matrice[edi-16][esi]
	
	cmp eax,ebx
	je dublare_1_jos
	jmp eroare_spatiu_1_jos
	
mutare_jos_2_spatii_1:
	mov eax,matrice[edi-32][esi]
	mov matrice[edi][esi],eax
	mov matrice[edi-32][esi],80
	jmp eroare_spatiu_1_jos

eroare_spatiu_1_jos:
	add esi,4
	cmp pix,0
	je miscare_jos_2
	jmp eticheta_1_jos
	
miscare_jos_2:
	mov edi,16
	mov esi,0
	mov pix,4
	jmp eticheta_2_jos
	
eticheta_2_jos:
	cmp eax,75
	je mesaj
	dec pix
	mov eax,matrice[edi][esi]
	mov ebx,matrice[edi-16][esi]
	cmp eax,80
	je dublare_spatiu_2_jos
	cmp eax,ebx
	je dublare_2_jos
	
	add esi,4
	cmp pix,0
	jne eticheta_2_jos
	jmp final_apasare_butoane
	
dublare_2_jos:
	cmp eax,80
	je eroare_spatiu_2_jos
    add matrice[edi][esi],1
	mov matrice[edi-16][esi],80
	add esi,4
	cmp pix,0
	je final_apasare_butoane
	jmp eticheta_2_jos

dublare_spatiu_2_jos:
	mov ecx,matrice[edi-16][esi]
	mov matrice[edi][esi],ecx
	mov matrice[edi-16][esi],80
	jmp eroare_spatiu_2_jos
	
eroare_spatiu_2_jos:
	add esi,4
	cmp pix,0
	je final_apasare_butoane
	jmp eticheta_2_jos

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;initializare
miscare_dreapta:
	mov edi,0
	mov esi,12
	mov pix,4
	jmp eticheta_dreapta

;parcurgere primele 2 linii 
	
eticheta_dreapta:
	dec pix
	mov eax,matrice[edi][esi]
	mov ebx,matrice[edi][esi-4]
	mov ecx,matrice[edi][esi-8]
	
	cmp eax,75
	je mesaj
	
	cmp eax,80
	je dublare_spatiu_dreapta
	
	cmp ebx,80
	je caz_special_dreapta
	
	cmp ecx,80
	je caz_special_dreapta_3
	
	cmp eax,ebx
	je dublare_dreapta
	
	add edi,16
	cmp pix,0
	jne eticheta_dreapta
	jmp miscare_dreapta_1
	
;4 0 2 2	
caz_special_dreapta:
	mov ecx,matrice[edi][esi-8]
	cmp ecx,80
	je caz_special_1_dreapta
	
	mov matrice[edi][esi-4], ecx
	mov ecx,matrice[edi][esi-12]
	mov matrice[edi][esi-8],ecx
	mov matrice[edi][esi-12],80
	
	mov eax,matrice[edi][esi]
	mov ebx,matrice[edi][esi-4]
	cmp eax,ebx
	je dublare_dreapta

	jmp eroare_spatiu_dreapta
	
;2 0 0 2
caz_special_1_dreapta:
	mov ecx,matrice[edi][esi-12]
	mov matrice[edi][esi-4],ecx
	mov matrice[edi][esi-12],80
	mov eax,matrice[edi][esi]
	mov ebx,matrice[edi][esi-4]
	cmp eax,ebx
	je dublare_dreapta
	jmp eroare_spatiu_dreapta

;0 2 0 2
caz_special_2_dreapta:
	mov ecx,matrice[edi][esi-4]
	mov matrice[edi][esi-4],80
	mov matrice[edi][esi],ecx
	mov edx,matrice[edi][esi-12]
	mov matrice[edi][esi-12],80
	mov matrice[edi][esi-4],edx
	mov eax,matrice[edi][esi]
	mov ebx,matrice[edi][esi-4]
	cmp eax,ebx
	je dublare_dreapta
	jmp eroare_spatiu_dreapta

caz_special_dreapta_3:;  2 0 2 4
	mov ecx,matrice[edi][esi-12]
	mov matrice[edi][esi-8], ecx
	mov matrice[edi][esi-12],80
	
	mov eax,matrice[edi][esi]
	mov ebx,matrice[edi][esi-4]
	cmp eax,ebx
	je dublare_dreapta
	jmp eroare_spatiu_dreapta
	
;daca avem match pe primele 2 coloane facem adunarea
dublare_dreapta:
	
	cmp eax,80
	je eroare_spatiu_dreapta
    add matrice[edi][esi],1
	mov eax,matrice[edi][esi-8]
	mov ebx,matrice[edi][esi-12]
	mov matrice[edi][esi-4],eax
	mov matrice[edi][esi-8],ebx
	mov matrice[edi][esi-12],80
	add edi,16
	cmp pix,0
	jne eticheta_dreapta
	jmp miscare_dreapta_1
	
; cazul in care avem pe prima  coloana un spatiu
dublare_spatiu_dreapta:
	cmp ebx,80
	je mutare_dreapta_2_spatii
	
	mov edx,matrice[edi][esi-8]
	cmp edx,80
	je caz_special_2_dreapta
	
	mov ecx,matrice[edi][esi-4]
	mov matrice[edi][esi],ecx
	mov edx,matrice[edi][esi-8]
	mov matrice[edi][esi-4],edx
	mov ecx,matrice[edi][esi-12]
	mov matrice[edi][esi-8],ecx
	mov matrice[edi][esi-12],80
	
	
	mov eax,matrice[edi][esi]
	mov ebx,matrice[edi][esi-4]
	cmp eax,ebx
	je dublare_dreapta
	jmp eroare_spatiu_dreapta


; avem spatiu pe prima si a 2 a coloana	
mutare_dreapta_2_spatii:
	mov ecx,matrice[edi][esi-8]
	cmp ecx,80
	je mutare_sus_3_spatii_dreapta
	mov matrice[edi][esi],ecx
	mov edx,matrice[edi][esi-12]
	mov matrice[edi][esi-4],edx
	mov matrice[edi][esi-8],80
	mov matrice[edi][esi-12],80
	
	mov eax,matrice[edi][esi]
	mov ebx,matrice[edi][esi-4]
	
	cmp eax,ebx
	je dublare_dreapta
	jmp eroare_spatiu_dreapta

;primele 3 coloane sunt spatii	
mutare_sus_3_spatii_dreapta:
	mov ecx,matrice[edi][esi-12]
	mov matrice[edi][esi],ecx
	mov matrice[edi][esi-4],80
	mov matrice[edi][esi-8],80
	mov matrice[edi][esi-12],80
	jmp eroare_spatiu_dreapta
	
;incrementare la linia urmatoare	
eroare_spatiu_dreapta:
	add edi,16
	cmp pix,0
	je miscare_dreapta_1
	jmp eticheta_dreapta

	
miscare_dreapta_1:
	mov edi,0
	mov esi,8
	mov pix,4
	jmp eticheta_1_dreapta

eticheta_1_dreapta:
	dec pix
	mov eax,matrice[edi][esi]
	mov ebx,matrice[edi][esi-4]
	
	cmp eax,75
	je mesaj
	
	cmp eax,80
	je dublare_spatiu_1_dreapta
	
	cmp eax,ebx
	je dublare_1_dreapta
	
	add edi,16
	cmp pix,0
	jne eticheta_1_dreapta
	jmp miscare_2_dreapta
	
dublare_1_dreapta:
	cmp eax,80
	je eroare_spatiu_1_dreapta
    add matrice[edi][esi],1
	mov ebx,matrice[edi][esi-8]
	mov matrice[edi][esi-4],ebx
	mov matrice[edi][esi-8],80
	mov eax,matrice[edi][esi]
	mov ebx,matrice[edi][esi-4]
	add edi,16
	cmp pix,0
	je miscare_2_dreapta
	jmp eticheta_1_dreapta

dublare_spatiu_1_dreapta:
	cmp ebx,80
	je mutare_sus_2_spatii_1_dreapta
	mov ecx,matrice[edi][esi-4]
	mov matrice[edi][esi],ecx
	mov edx,matrice[edi][esi-8]
	mov matrice[edi][esi-4],edx
	mov matrice[edi][esi-8],80
	
	mov eax,matrice[edi][esi]
	mov ebx,matrice[edi][esi-4]
	
	cmp eax,ebx
	je dublare_1_dreapta
	jmp eroare_spatiu_1_dreapta
	
mutare_sus_2_spatii_1_dreapta:
	mov eax,matrice[edi][esi-8]
	mov matrice[edi][esi],eax
	mov matrice[edi][esi-8],80
	jmp eroare_spatiu_1_dreapta

eroare_spatiu_1_dreapta:
	add edi,16
	cmp pix,0
	je miscare_2_dreapta
	jmp eticheta_1_dreapta
	
miscare_2_dreapta:
	mov edi,0
	mov esi,4
	mov pix,4
	jmp eticheta_2_dreapta
	
eticheta_2_dreapta:
	dec pix
	mov eax,matrice[edi][esi]
	mov ebx,matrice[edi][esi-4]
	
	cmp eax,75
	je mesaj
	
	cmp eax,80
	je dublare_spatiu_2_dreapta
	cmp eax,ebx
	je dublare_2_dreapta
	
	add edi,16
	cmp pix,0
	jne eticheta_2_dreapta
	jmp final_apasare_butoane
	
dublare_2_dreapta:
	cmp eax,80
	je eroare_spatiu_2_dreapta
    add matrice[edi][esi],1
	mov matrice[edi][esi-4],80
	add edi,16
	cmp pix,0
	je final_apasare_butoane
	jmp eticheta_2_dreapta

dublare_spatiu_2_dreapta:
	mov ecx,matrice[edi][esi-4]
	mov matrice[edi][esi],ecx
	mov matrice[edi][esi-4],80
	jmp eroare_spatiu_2_dreapta
	
eroare_spatiu_2_dreapta:
	add edi,16
	cmp pix,0
	je final_apasare_butoane
	jmp eticheta_2_dreapta	
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
	
mesaj:
	make_text_macro 'Q', area, 360, 360 
	jmp return
	
final_apasare_butoane:
	;;;;;;random
		
			inc counter
			inc counter1

			mov eax, counter
			and eax,3
			
			mov ebx, counter1
			and ebx,3
			
			mov cl,16
			mul cl
			
			mov edi,eax
				
			mov eax,ebx
			mov cl,4
			mul cl
			mov esi,eax
			
			mov eax, matrice[edi][esi]
			cmp eax,80
			jne et_random
			
			mov ecx,4
			mov ajutor1,ecx
			mov ajutor2,ecx
			
			push esi
			push offset format1
			call printf
			add esp,8
			
			push edi
			push offset format1
			call printf
			add esp,8
			
			mov matrice[edi][esi], 65 

	  jmp return
	  
	  et_random:
	  mov ecx, ajutor1
	  cmp ecx, 0
	  je et_random_1
	  inc counter
	  dec ajutor1
	  jmp final_apasare_butoane
	  
	  et_random_1:
	  mov ecx, ajutor2
	  cmp ecx,0
	  je return
	  dec ajutor2
 	  inc counter1
	  mov ecx,4
	  mov ajutor1,ecx
      jmp final_apasare_butoane	  
	 
return:
	  ret
apasare_butoane endp


start:
	;alocam memorie pentru zona de desenat
	mov eax, area_width
	mov ebx, area_height
	mul ebx
	shl eax, 2
	push eax
	call malloc
	add esp, 4
	mov area, eax
	;apelam functia de desenare a ferestrei
	; typedef void (*DrawFunc)(int evt, int x, int y);
	; void __cdecl BeginDrawing(const char *title, int width, int height, unsigned int *area, DrawFunc draw);
	push offset draw
	push area
	push area_height
	push area_width
	push offset window_title
	call BeginDrawing
	add esp, 20
	
	push 0
	call exit
end start