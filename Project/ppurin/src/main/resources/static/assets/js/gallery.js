// gallery.html에서 좋아요 버튼 기능 추가
document.querySelectorAll('.like-btn').forEach(button => {
    button.addEventListener('click', function () {
        const photoId = this.dataset.photoId; // 각 사진의 ID
        const isLiked = this.classList.contains('liked'); // 좋아요 상태 확인

        if (isLiked) {
            // 좋아요 취소
            fetch(`/api/like/${photoId}`, { method: 'DELETE' })
                .then(response => {
                    if (response.ok) {
                        this.classList.remove('liked');
                        this.textContent = '❤️ 좋아요';
                    }
                })
                .catch(error => console.error("좋아요 취소 오류:", error));
        } else {
            // 좋아요 추가
            fetch(`/api/like`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ photoId })
            })
                .then(response => {
                    if (response.ok) {
                        this.classList.add('liked');
                        this.textContent = '💖 좋아요 취소';
                    }
                })
                .catch(error => console.error("좋아요 추가 오류:", error));
        }
    });
});